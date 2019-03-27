package com.sophos.poc.login.model.security;

public class RequestHeader {
	
	public RequestHeader() {}
	
	public RequestHeader(String token) {
		super();
		this.token = token;
	}

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
