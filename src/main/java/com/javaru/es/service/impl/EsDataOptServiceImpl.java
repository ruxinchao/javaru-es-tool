package com.javaru.es.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.javaru.es.config.ElasticSearchConfig;
import com.javaru.es.service.IEsDataOptService;
import com.javaru.es.util.ExceptionUtils;

@Service
public class EsDataOptServiceImpl implements IEsDataOptService {
	private static Logger logger = LoggerFactory.getLogger(EsDataOptServiceImpl.class);

	@Autowired
	private RestHighLevelClient esClient;
	@Autowired
	private ElasticSearchConfig esConfig;

	@Override
	public String adddata(String indexName, JSONObject json) {
		try {
			IndexRequest request = new IndexRequest(indexName);
			// 等待刷新完再返回
			request.setRefreshPolicy(this.getEsResearchStrategy());
			String id = json.getString("id");
			if (StringUtils.isBlank(id)) {
				id = UUID.randomUUID().toString();
			}

			request.index(indexName).id(id).source(json, XContentType.JSON);
			IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
			logger.info("elastic 数据新增成功{}" + indexName);
			return response.getId();
		} catch (Exception e) {
			logger.error("数据变更失败:{}", e);
			ExceptionUtils.throwBizException("es数据保存异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<IndexResponse> adddatas(String indexName, List<JSONObject> jsons) {
		List<IndexResponse> list = new ArrayList<>();
		try {
			BulkRequest bulkRequest = new BulkRequest();
			// 等待刷新完再返回
			bulkRequest.setRefreshPolicy(this.getEsResearchStrategy());
			IndexRequest request = null;
			for (JSONObject json : jsons) {
				if (StringUtils.isNotBlank(json.getString("indexName"))) {
					indexName = json.getString("indexName");
				}
				request = new IndexRequest(indexName);
				String id = json.getString("id");
				if (StringUtils.isBlank(id)) {
					id = UUID.randomUUID().toString();
				}
				request.index(indexName).id(id).source(json, XContentType.JSON);
				bulkRequest.add(request);
			}
			BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			for (BulkItemResponse bulkItemResponse : response) {
				DocWriteResponse itemResponse = bulkItemResponse.getResponse();
				switch (bulkItemResponse.getOpType()) {
				case INDEX:
					IndexResponse response1 = (IndexResponse) itemResponse;
					list.add(response1);
					break;
				case CREATE:
					IndexResponse indexResponse = (IndexResponse) itemResponse;
					list.add(indexResponse);
					break;
				default:
					break;
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("批量插入失败:{}", e);
			ExceptionUtils.throwBizException("es数据批量插入异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public String updateData(String indexName, JSONObject json) {
		try {
			String id = json.getString("id");
			if (StringUtils.isBlank(id)) {
				ExceptionUtils.throwBizException("更新ES数据ID IS NULL");
			}
			UpdateRequest request = new UpdateRequest(indexName, id);
			// 设置刷新策略
			request.setRefreshPolicy(getEsResearchStrategy());
			request.doc(json, XContentType.JSON);
			UpdateResponse updateResponse = esClient.update(request, RequestOptions.DEFAULT);
			logger.info("elastic 更新数据成功{}" + indexName);
			return updateResponse.getId();
		} catch (Exception e) {
			logger.error("elastic 更新数据失败:{}", e);
			ExceptionUtils.throwBizException("es数据更新异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<UpdateResponse> updateDatas(String indexName, List<JSONObject> jsons) {
		List<UpdateResponse> list = new ArrayList<>();
		try {
			BulkRequest bulkRequest = new BulkRequest();
			// 等待刷新完再返回
			bulkRequest.setRefreshPolicy(getEsResearchStrategy());
			for (JSONObject json : jsons) {
				UpdateRequest request = new UpdateRequest(indexName, json.getString("id"));
				request.doc(json, XContentType.JSON);
				bulkRequest.add(request);
			}
			BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			for (BulkItemResponse bulkItemResponse : response) {
				DocWriteResponse itemResponse = bulkItemResponse.getResponse();
				switch (bulkItemResponse.getOpType()) {
				case UPDATE:
					UpdateResponse updateResponse = (UpdateResponse) itemResponse;
					list.add(updateResponse);
					break;
				default:
					break;
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("批量更新数据失败:{}", e);
			ExceptionUtils.throwBizException("es数据批量更新异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public Long updateDatasByQuery(String indexName, JSONObject jsonData, JSONObject queryData) {
		try {
			UpdateByQueryRequest request = new UpdateByQueryRequest(indexName);
			request.setBatchSize(10000);
			// 默认情况下，版本冲突会中止该UpdateByQueryRequest过程，但您可以使用以下命令对其进行继续
			request.setConflicts("proceed");
			// 条件
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			for (String key : queryData.keySet()) {
				boolQueryBuilder.must(QueryBuilders.matchQuery(key, queryData.get(key)));
			}
			request.setQuery(boolQueryBuilder);
			StringBuffer scriptUpData = new StringBuffer();
			// 更新的值
			for (String key : jsonData.keySet()) {
				scriptUpData.append("ctx._source['" + key + "']='" + jsonData.get(key) + "';");
			}

			request.setScript(
					new Script(ScriptType.INLINE, "painless", scriptUpData.toString(), new HashMap<String, Object>()));
			BulkByScrollResponse bulkResponse = esClient.updateByQuery(request, RequestOptions.DEFAULT);
			return bulkResponse.getTotal();
		} catch (Exception e) {
			logger.error("updateDatasByQuery失败:{}", e);
			ExceptionUtils.throwBizException("esupdateDatasByQuery异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public String deleteById(String indexName, String id) {
		if (StringUtils.isBlank(id)) {
			ExceptionUtils.throwBizException("id is null");
		}
		try {
			DeleteRequest request = new DeleteRequest(indexName, id);
			// 等待刷新完再返回
			request.setRefreshPolicy(getEsResearchStrategy());
			DeleteResponse deleteResponse = esClient.delete(request, RequestOptions.DEFAULT);
			return deleteResponse.getId();
		} catch (Exception e) {
			logger.error("索引数据变更失败:{}", e);
			ExceptionUtils.throwBizException("es数据删除异常" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<DeleteResponse> deleteByIds(String indexName, List<String> ids) {
		List<DeleteResponse> list = new ArrayList<>();
		try {
			BulkRequest bulkRequest = new BulkRequest();
			// 刷新策略
			bulkRequest.setRefreshPolicy(getEsResearchStrategy());
			for (String id : ids) {
				if (StringUtils.isBlank(id)) {
					ExceptionUtils.throwBizException("id is null");
				}
				DeleteRequest request = new DeleteRequest(indexName, id);
				bulkRequest.add(request);
			}
			BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);

			for (BulkItemResponse bulkItemResponse : response) {
				DocWriteResponse itemResponse = bulkItemResponse.getResponse();
				switch (bulkItemResponse.getOpType()) {
				case DELETE:
					DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
					list.add(deleteResponse);
					break;
				default:
					break;
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("批量刪除异常:{}", e);
			return null;
		}
	}

	@Override
	public Long deleteByQuery(String indexName, JSONObject queryData) {
		try {
			DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
			request.setBatchSize(10000);
			// 默认情况下，版本冲突会中止该UpdateByQueryRequest过程，但您可以使用以下命令对其进行继续
			request.setConflicts("proceed");
			// 条件
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			for (String key : queryData.keySet()) {
				boolQueryBuilder.must(QueryBuilders.matchQuery(key, queryData.get(key)));
			}
			request.setQuery(boolQueryBuilder);
			BulkByScrollResponse bulkResponse = esClient.deleteByQuery(request, RequestOptions.DEFAULT);
			return bulkResponse.getTotal();
		} catch (Exception e) {
			logger.error("根据条件刪除异常:{}", e);
			return null;
		}
	}

	/**
	 * 获取Es刷新策略
	 * 
	 * @return
	 */
	private RefreshPolicy getEsResearchStrategy() {
		// RefreshPolicy#IMMEDIATE:
		// 请求向ElasticSearch提交了数据，立即进行数据刷新，然后再结束请求。
		// 优点：实时性高、操作延时短。
		// 缺点：资源消耗高。
		// RefreshPolicy#WAIT_UNTIL:
		// 请求向ElasticSearch提交了数据，等待数据完成刷新，然后再结束请求。
		// 优点：实时性高、操作延时长。
		// 缺点：资源消耗低。
		// RefreshPolicy#NONE:
		// 默认策略。
		// 请求向ElasticSearch提交了数据，不关系数据是否已经完成刷新，直接结束请求。
		// 优点：操作延时短、资源消耗低。
		// 缺点：实时性低。
		// return RefreshPolicy.IMMEDIATE;
		return RefreshPolicy.parse(esConfig.getRefreshPolicy());
	}

}
