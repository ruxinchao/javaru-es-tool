package com.javaru.es.vo.response;

/**
 * Esql 分页深度查询
 * 
 * @author rxc
 *
 */
public class EsQuerySqlResultPage extends EsResultBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 游标 类C语言下表
	private String cursor;

	public EsQuerySqlResultPage() {
		super();
	}

	public EsQuerySqlResultPage(String cursor) {
		super();
		this.cursor = cursor;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	
	public EsQuerySqlResultPage setEsResultSqlQueryPage(EsResultBase EsResultBase, String cursor) {
		this.setEsData(EsResultBase.getEsData());
		this.setColumns(EsResultBase.getColumns());
		this.setCode(EsResultBase.getCode());
		this.setMsg(EsResultBase.getMsg());
		this.setCursor(cursor);
		return this;
	}

}
