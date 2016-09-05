package com.pw.lokalizator.model.security;

import javax.ws.rs.FormParam;

public class Credentials {
	@FormParam("login")
	private String login;
	@FormParam("password")
	private String password;

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
