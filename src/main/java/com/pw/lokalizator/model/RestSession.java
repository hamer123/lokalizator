package com.pw.lokalizator.model;

import java.util.Date;
import java.util.Map;

import com.pw.lokalizator.model.entity.User;

public class RestSession {
	public static final String REST_SESSION_ATR = "j_rest_session";
	private Date lastUsed;
	private User user;
	private Map<String,Object>sessionAttributes;
	
	
	public RestSession(User user) {
		this.lastUsed = new Date();
		this.user = user;
	}
	
	public Date getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
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

	public Object getAttribute(String name){
		return sessionAttributes.get(name);
	}
	
}
