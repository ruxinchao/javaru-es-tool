package com.javaru.es.vo.exception;

import com.javaru.es.vo.response.EsResultBase;

/**
 * 异常信息
 */
public class BizException extends RuntimeException {
	private static final long serialVersionUID = 7430449458407101783L;
	private String message;
	private String code;

	public BizException() {
		super();
	}
	
	public BizException(String message) {
		super();
		this.message = message;
		this.code = EsResultBase.ERROR_CODE;
	}

	public BizException(String code, String message) {
		super();
		this.message = message;
		this.code = code;
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}
}