package com.sophos.poc.login.model;

public class LoginResponse {
	
	private String approvalCode;

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public LoginResponse(String approvalCode) {
		super();
		this.approvalCode = approvalCode;
	}
	
	
}
