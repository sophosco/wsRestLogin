	package com.sophos.poc.login.model.security;

public class TokenRequest {
	
	private RequestHeader requestHeader;
	private RequestPayload requestPayload;
	
	public TokenRequest() {}
	
	
	public RequestHeader getRequestHeader() {
		return requestHeader;
	}
	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}
	public RequestPayload getRequestPayload() {
		return requestPayload;
	}
	public void setRequestPayload(RequestPayload requestPayload) {
		this.requestPayload = requestPayload;
	}
	
	
	

}
