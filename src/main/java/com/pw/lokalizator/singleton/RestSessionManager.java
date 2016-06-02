package com.pw.lokalizator.singleton;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.entity.User;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class RestSessionManager {
	Logger logger = Logger.getLogger(RestSessionManager.class);
	private Map<String,RestSession>restSessions = new ConcurrentHashMap<String,RestSession>();
	
	public RestSession getRestSession(String token){
		return restSessions.get(token);
	}
	
	public Set<String>tokens(){
		return restSessions.keySet();
	}
	
	public boolean isTokenArleadyUse(String token){
		return restSessions.keySet().contains(token);
	}
	
	public String getTokenForLogin(String login){
		for(String token : restSessions.keySet()){
			if (restSessions.get(token).getUser().getLogin().equals(login))
				return token;
		}
		return null;
	}
	
	public void addRestSession(String token, User user){

		RestSession session = new RestSession(user);
		restSessions.put(token, session);
		logger.info("[RestSessionSimulator] nowa RESTSession :"  + user.getLogin());
	}
	
	public boolean invalidationRestSession(String token){
		RestSession restSession = null;
		if((restSession = restSessions.remove(token)) != null){
			logger.info("[RestSessionSimulator] invalidationRestSession dla :" + restSession.getUser().getLogin());
			return true;
		}
		return false;
	}
	
	@AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
	public Collection<RestSession> getRestSessionsCollection(){
		return restSessions.values();
	}
	
	public List<String>getUserOnlineLogins(){
		Collection<RestSession>restSessions = getRestSessionsCollection();
		
		return restSessions.stream()
				           .map(rs -> rs.getUser().getLogin())
				           .collect(Collectors.toList());
	}
	
	public boolean isUserOnline(String login){
		Optional<RestSession> optionalRestSession = restSessions
				.values()
				.stream()
				.filter( rs -> rs.getUser().getLogin().equals(login))
				.findFirst();
		
		try{
			RestSession restSession = optionalRestSession.get();
			long time = 2 * 60 * 1000; //2min
			
			if(olderTwoMinutesThanCurrentDate(restSession.getLastUsed()))
				return true;
		}catch(NoSuchElementException e){
		}
		
		return false;
	}
	
	private boolean olderTwoMinutesThanCurrentDate(Date date){
		Date now = new Date();
		long twoMin = 2 * 60 * 1000;
		return now.getTime() - twoMin < date.getTime(); 
	}
}
