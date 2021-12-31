package com.javaru.es.vo.request;

import java.util.List;

public class IndexQueryRequest extends EsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * 字段名称 集合
	 */
	private List<String> fieldNames;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

}
