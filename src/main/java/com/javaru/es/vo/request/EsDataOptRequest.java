package com.javaru.es.vo.request;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class EsDataOptRequest extends EsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 单个 add or update 使用
	private JSONObject data;
	// 批量 add or update 使用
	private List<JSONObject> dataList;
	// 根据条件查询使用
	private JSONObject queryParms;

	// id删除
	private String id;
	// id 批量删除
	private List<String> ids;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public List<JSONObject> getDataList() {
		return dataList;
	}

	public void setDataList(List<JSONObject> dataList) {
		this.dataList = dataList;
	}

	public JSONObject getQueryParms() {
		return queryParms;
	}

	public void setQueryParms(JSONObject queryParms) {
		this.queryParms = queryParms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

}
