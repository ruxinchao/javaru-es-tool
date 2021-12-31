package com.javaru.es.vo.request.query;

import java.io.Serializable;

import com.javaru.es.annotation.enums.FType;
/**
 * 查询字段对象
 * @author rxc
 *
 */
public class QueryCommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 字段名称
	private String filedName;
	// 字段值
	private String filedvalue;
	// 字段类型
	private FType filedType;

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getFiledvalue() {
		return filedvalue;
	}

	public void setFiledvalue(String filedvalue) {
		this.filedvalue = filedvalue;
	}

	public FType getFiledType() {
		return filedType;
	}

	public void setFiledType(FType filedType) {
		this.filedType = filedType;
	}

}
