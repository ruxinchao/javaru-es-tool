package com.javaru.es.vo.request.query;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortOrder;

import com.javaru.es.vo.request.EsBaseRequest;

public class QueryBeanAll extends EsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyword;

	// 排序字段
	private String sortField;
	// 正序还是倒叙
	private String sort = "asc";
	// 每页大小
	private Integer size = 20;
	// 当前页
	private Integer page = 1;
	// 自己的查询bean 使用我们的注解
	private List<QueryCommonVo> queryParms;
	// 查询返回的列，不传全部
	private String[] includeFields;
	// 返回排除的列
	private String[] excludeFields;
	// 全文搜索条件 queryString
	private String queryString;

	public SortOrder getSortOrder() {
		if (StringUtils.lowerCase(this.sort).equals("desc")) {
			return SortOrder.DESC;
		}
		return SortOrder.ASC;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String[] getIncludeFields() {
		return includeFields;
	}

	public void setIncludeFields(String[] includeFields) {
		this.includeFields = includeFields;
	}

	public String[] getExcludeFields() {
		return excludeFields;
	}

	public void setExcludeFields(String[] excludeFields) {
		this.excludeFields = excludeFields;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<QueryCommonVo> getQueryParms() {
		return queryParms;
	}

	public void setQueryParms(List<QueryCommonVo> queryParms) {
		this.queryParms = queryParms;
	}

}
