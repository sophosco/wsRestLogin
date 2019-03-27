package com.sophos.poc.login.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;

public class Users {
	
	@PrimaryKey
	private String email;
	private String idSesion;
	private String password;
	
	
	public Users() {}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}

	public Users(String idSesion, String password, String email) {
		super();
		this.idSesion = idSesion;
		this.password = password;
		this.email = email;
	}

}
