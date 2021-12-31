package com.javaru.es.vo.request;

import java.util.List;

import com.javaru.es.vo.IndexFieldVo;

public class IndexAddRequest extends EsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 索引分区的数量。shards分区
	private int shards = 5;
	// 副本数量
	private int replicas = 1;
	// 字段list
	private List<IndexFieldVo> fieldList;

	public int getShards() {
		return shards;
	}

	public void setShards(int shards) {
		this.shards = shards;
	}

	public int getReplicas() {
		return replicas;
	}

	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}

	public List<IndexFieldVo> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<IndexFieldVo> fieldList) {
		this.fieldList = fieldList;
	}

}
