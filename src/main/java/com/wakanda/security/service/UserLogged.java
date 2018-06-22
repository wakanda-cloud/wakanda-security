package com.wakanda.security.service;

public class UserLogged {

	private String email;
	private String token;
	
	public UserLogged(String email, String token) {
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static String getId(String email) {
		return "userLogged_" + email;
	}
}
