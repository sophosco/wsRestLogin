package com.sophos.poc.login.model.security;

public class ResponseHeader {
	
	private ResponseHeader responseInfo;
	private Status status;
	private ResponsePayload responsePayload;
	
	public ResponseHeader() {}

	public ResponseHeader getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseHeader responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ResponsePayload getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(ResponsePayload responsePayload) {
		this.responsePayload = responsePayload;
	}
	
	

}

