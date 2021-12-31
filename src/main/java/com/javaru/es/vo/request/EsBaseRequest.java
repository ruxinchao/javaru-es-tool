package com.javaru.es.vo.request;

import java.io.Serializable;

public class EsBaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// es 索引名称  可以 test*
	private String indexName;

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

}
