package com.jd.common.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 959923676353015439L;

	private String msg;

	public ServiceException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
