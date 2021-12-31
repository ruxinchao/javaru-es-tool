package com.javaru.es.vo.response;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * Es 查询返回base 类
 * 
 * @author rxc
 *
 */
public class EsResultBase implements Serializable {
	/**
	 * 
	 */
	public static final String SUCCESS_CODE = "0000";
	public static final String SUCCESS_MSG = "成功";
	public static final String FAIL_CODE = "1000"; // 失败情况
	public static final String ERROR_CODE = "5000"; // 异常情况
	private static final long serialVersionUID = 1L;
	private String code = SUCCESS_CODE;
	private String msg = SUCCESS_MSG;

	public EsResultBase() {
		super();
	}

	public EsResultBase(List<JSONObject> esData) {
		super();
		this.code = SUCCESS_CODE;
		this.msg = SUCCESS_MSG;
		this.esData = esData;
	}

	public EsResultBase(String code, String msg, List<JSONObject> esData) {
		super();
		this.code = code;
		this.msg = msg;
		this.esData = esData;
	}

	private List<JSONObject> esData;

	private List<JSONObject> columns;

	public List<JSONObject> getEsData() {
		return esData;
	}

	public void setEsData(List<JSONObject> esData) {
		this.esData = esData;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<JSONObject> getColumns() {
		return columns;
	}

	public void setColumns(List<JSONObject> columns) {
		this.columns = columns;
	}

}
