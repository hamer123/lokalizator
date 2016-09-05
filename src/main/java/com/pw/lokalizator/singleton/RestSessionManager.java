package com.pw.lokalizator.singleton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

import org.jboss.resteasy.logging.Logger;
import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.model.entity.User;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class RestSessionManager {
	Logger logger = Logger.getLogger(RestSessionManager.class);
	private Map<String,RestSession> sessions = new ConcurrentHashMap<String,RestSession>();

	public Set<String>tokens(){
		return sessions.keySet();
	}
	
	public String getTokenForLogin(String login){
		for(String token : sessions.keySet()){
			if (sessions.get(token).getUser().getLogin().equals(login))
				return token;
		}
		return null;
	}

	public RestSession addSession(User user){
		RestSession restSession = getSessionByUser(user);
		if(restSession != null){
			return restSession;
		} else {
			UUID uuid = UUID.randomUUID();
			restSession = new RestSession(user);
			restSession.setToken(uuid.toString());
			restSession.setLastUsed(new Date());

			sessions.put(uuid.toString(), restSession);
			return restSession;
		}
	}

	public RestSession getSession(String token){
		return sessions.get(token);
	}

	public RestSession getSessionByUser(User user){
		for(RestSession session : sessions.values()){
			if(session.getUser().getId() == user.getId())
				return session;
		}
		return null;
	}
	
	public boolean invalidationRestSession(String token){
		RestSession restSession = null;
		if((restSession = sessions.remove(token)) != null){
			logger.info("[RestSessionSimulator] invalidationRestSession dla :" + restSession.getUser().getLogin());
			return true;
		}
		return false;
	}
	
	@AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
	public Collection<RestSession> getRestSessionsCollection(){
		return sessions.values();
	}
	
	public List<String>getUserOnlineLogins(){
		Collection<RestSession>restSessions = getRestSessionsCollection();
		
		return restSessions.stream()
				           .map(rs -> rs.getUser().getLogin())
				           .collect(Collectors.toList());
	}
	
	public boolean isUserOnline(String login){
		try{
			Optional<RestSession> optionalRestSession = sessions
					.values()
					.stream()
					.filter( rs -> rs.getUser().getLogin().equals(login))
					.findFirst();
			
			optionalRestSession.get();			
			return true;
		}catch(NoSuchElementException e){
			//
		}
		
		return false;
	}
}
