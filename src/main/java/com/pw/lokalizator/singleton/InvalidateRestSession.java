package com.pw.lokalizator.singleton;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSession;

@Singleton
public class InvalidateRestSession {
	@EJB
	private RestSessionManager restSessionSimulator;
	Logger logger = Logger.getLogger(InvalidateRestSession.class);
	
	@Schedule(minute="*/5",hour="*", persistent=false)
	public void timeoutRestSession(){
		logger.info("[InvalidateRestSession] InvalidateRestSession job has started");
		
		for(String token : restSessionSimulator.tokens()){
			RestSession restSession = restSessionSimulator.getRestSession(token);
			if(isPassedTime(restSession.getLastUsed()))
				restSessionSimulator.invalidationRestSession(token);
		}
	}
	
	public boolean isPassedTime(Date date){
		final long timeout = 5 * 60 * 1000; // 5min
		long currentTime = new Date().getTime();
		
		return currentTime - timeout > date.getTime();
	}
}
