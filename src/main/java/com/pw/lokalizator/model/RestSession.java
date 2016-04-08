package com.pw.lokalizator.model;

import java.util.Date;
import java.util.Map;

public class RestSession {
	public static final String REST_SESSION_ATR = "j_rest_session";
	private Date lastUsed;
	private String authToken;
	private User user;
	private Map<String,Object>sessionAttributes;

	public RestSession() {}
	
	public RestSession(Date lastUsed, String authToken, User user) {
		this.lastUsed = lastUsed;
		this.authToken = authToken;
		this.user = user;
	}
	
	public Date getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> getSessionAttributes() {
		return sessionAttributes;
	}

	public void setSessionAttributes(Map<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}
	
	
}
