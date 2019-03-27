package com.sophos.poc.login.model.security;

import java.util.Date;

public class ResponseInfo {
	
	private String system;
	private Date responseDate;
	
	public ResponseInfo() {}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	

}
