package com.pw.lokalizator.job;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.singleton.RestSessionManager;

@Singleton
public class InvalidateRestSession {
	@Inject
	private RestSessionManager restSessionSimulator;
	@Inject
	private Logger logger;
	
	@Schedule(minute="*/1",hour="*", persistent=false)
	public void timeoutRestSession(){
		logger.info("[InvalidateRestSession] job has started");
		
		for(String token : restSessionSimulator.tokens()){
			RestSession restSession = restSessionSimulator.getRestSession(token);
			if(isPassedTime(restSession.getLastUsed()))
				restSessionSimulator.invalidationRestSession(token);
		}
	}
	
	public boolean isPassedTime(Date date){
		final long oneMinute = 1 * 60 * 1000;
		long currentTime = new Date().getTime();
		
		return currentTime - oneMinute > date.getTime();
	}
}
