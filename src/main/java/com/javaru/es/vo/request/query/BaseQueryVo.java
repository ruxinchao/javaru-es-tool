package com.javaru.es.vo.request.query;

import java.io.Serializable;

import com.javaru.es.annotation.QueryType;
import com.javaru.es.annotation.enums.FType;

/**
 * 索引公共字段
 * @author rxc
 *
 */
public class BaseQueryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 * 开始结束时间中间使用分隔
	 */
	@QueryType(value = FType.DATE , separator=",")
	private String create_date;
	
	/**
	 * 创建时间
	 * 开始结束时间中间使用#分隔
	 */
	@QueryType(value = FType.DATE)
	private String update_date;
	
	/**
	 * 创建人
	 */
	@QueryType(value = FType.MATCHQUERY)
	private String create_user;
	
	/**
	 * 更新人
	 */
	@QueryType(value = FType.MATCHQUERY)
	private String update_user;

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}


}
