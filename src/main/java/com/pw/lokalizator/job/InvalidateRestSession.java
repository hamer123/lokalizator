package com.pw.lokalizator.job;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.singleton.RestSessionManager;

@Singleton
public class InvalidateRestSession {
	@EJB
	private RestSessionManager restSessionSimulator;
	Logger logger = Logger.getLogger(InvalidateRestSession.class);
	
	@Schedule(minute="*/1",hour="*", persistent=false)
	public void timeoutRestSession(){
		logger.info("[InvalidateRestSession] InvalidateRestSession job has started");
		
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
