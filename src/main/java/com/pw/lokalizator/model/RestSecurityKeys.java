package com.pw.lokalizator.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestSecurityKeys {
	@XmlElement
	private String serviceKey;
	@XmlElement
	private String authToken;
	
	public RestSecurityKeys() {
	}
	
	public RestSecurityKeys(String serviceKey, String authToken) {
		this.serviceKey = serviceKey;
		this.authToken = authToken;
	}
	
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}
