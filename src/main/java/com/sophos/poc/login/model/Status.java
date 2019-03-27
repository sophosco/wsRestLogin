package com.sophos.poc.login.model;

import java.io.Serializable;

public class Status implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	private String exception;
	private Object response;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public Status(String code, String message, String exception, Object response) {
		super();
		this.code = code;
		this.message = message;
		this.exception = exception;
		this.response = response;
	}

}
