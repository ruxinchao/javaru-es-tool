package com.javaru.es.service;

import java.util.List;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * Es 数据操作 增 删 改
 * 
 * @author rxc
 *
 */
public interface IEsDataOptService {
	// 新增数据 （注意如果json 中id 存在的话，没有传的字段则会更新为空）
	String adddata(String indexName, JSONObject json);

	/**
	 * 批量新增（注意如果json 中id 存在的话，没有传的字段则会更新为空）
	 * 
	 * @param indexName 索引
	 * @param jsons     批量新增的数据
	 * @return List<IndexResponse> 返回结果
	 */
	List<IndexResponse> adddatas(String indexName, List<JSONObject> jsons);

	// 更新
	String updateData(String indexName, JSONObject json);

	// 批量更新
	List<UpdateResponse> updateDatas(String indexName, List<JSONObject> jsons);

	/**
	 * 根据查询条件更新
	 * 
	 * @param indexName 索引
	 * @param jsonData  更新的数据 key 列 val 值
	 * @param queryData 查询的条件 key 列 val 值 （尽量使用keyword类型的字段 做key' ）
	 * @return
	 */
	Long updateDatasByQuery(String indexName, JSONObject jsonData, JSONObject queryData);

	// 根据id删除
	String deleteById(String indexName, String id);

	// 根基id批量删除
	List<DeleteResponse> deleteByIds(String indexName, List<String> ids);

	// 根据条件删除
	Long deleteByQuery(String indexName, JSONObject queryData);
}
