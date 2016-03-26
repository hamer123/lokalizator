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
	private RestSessionSimulator restSessionSimulator;
	Logger log = Logger.getLogger(InvalidateRestSession.class);
	
	@Schedule(minute="*/30",hour="*", persistent=false)
	public void timeoutRestSession(){
		log.info("Timer is starting to work");
		
		final long timeout = 10 * 60 * 1000; // 10min
		long currentTime = new Date().getTime();
		for(RestSession session : restSessionSimulator.getRestSessionsCollection())
			if(currentTime - timeout > session.getLastUsed().getTime()){
				restSessionSimulator.invalidationRestSession(session.getUser().getUserSecurity().getServiceKey());
			}
	}
}
