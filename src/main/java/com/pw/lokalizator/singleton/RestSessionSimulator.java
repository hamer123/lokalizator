package com.pw.lokalizator.singleton;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.User;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class RestSessionSimulator {
	Logger log = Logger.getLogger(RestSessionSimulator.class);
	private Map<String,RestSession>sessionMap = new HashMap<String,RestSession>();
	
	public RestSession getRestSession(String serviceKey){
		return sessionMap.get(serviceKey);
	}
	
	public RestSession createRestSession(String serviceKey, String authToken, User user){
		RestSession session = new RestSession(
				new Date(),
				authToken,
				user );
		sessionMap.put(serviceKey, session);
		
		log.info("SessionMap size " + sessionMap.size());
		
		return session;
	}
	
	public boolean invalidationRestSession(String serviceKey){
		if(sessionMap.remove(serviceKey) != null){
			log.info("invalidationRestSession for " + serviceKey);
			return true;
		}
		return false;
	}
	
	@AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
	public Collection<RestSession> getRestSessionsCollection(){
		return sessionMap.values();
	}
}
