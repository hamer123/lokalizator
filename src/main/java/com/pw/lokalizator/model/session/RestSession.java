package com.pw.lokalizator.model.session;

import java.util.Date;
import java.util.Map;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Roles;

public class RestSession {
	private Date lastUsed = new Date();
	private User user;
	private String token;
	private Map<String,Object>sessionAttributes;

	public RestSession(User user) {
		this.lastUsed = new Date();
		this.user = user;
	}

	public boolean isInRole(Roles role){
		for(Roles _role : user.getRoles())
			if(_role.equals(role))
				return true;
		return false;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setSessionAttributes(Map<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}
}
