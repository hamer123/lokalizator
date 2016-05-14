package com.pw.lokalizator.singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.User;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class RestSessionManager {
	Logger log = Logger.getLogger(RestSessionManager.class);
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
		
		log.info("[RestSessionSimulator] nowa sesja dla uzytkownika: "  + user.getLogin());
		
		return session;
	}
	
	public boolean invalidationRestSession(String serviceKey){
		if(sessionMap.remove(serviceKey) != null){
			log.info("[RestSessionSimulator] invalidationRestSession dla serviceKey: " + serviceKey);
			return true;
		}
		return false;
	}
	
	@AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
	public Collection<RestSession> getRestSessionsCollection(){
		return sessionMap.values();
	}
	
	public List<String>getUserOnlineLogins(){
		Collection<RestSession>restSessions = getRestSessionsCollection();
		
		return restSessions.stream()
				           .map(rs -> rs.getUser().getLogin())
				           .collect(Collectors.toList());
	}
}
