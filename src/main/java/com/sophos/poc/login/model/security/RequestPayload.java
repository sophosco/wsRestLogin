package com.sophos.poc.login.model.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestPayload {

	@JsonProperty( value="Id")
	private String Id;
	
	private Object payload;

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

}
