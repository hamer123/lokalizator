package com.pw.lokalizator.job;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import org.jboss.logging.Logger;
import com.pw.lokalizator.exception.AreaMialMessageException;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.serivce.qualifier.AreaMailMessage;
import com.pw.lokalizator.service.AreaMessageService;

@Stateless
public class AreaSendMailWorker {
	@Inject
	@AreaMailMessage 
	private AreaMessageService areaSendMessage;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private Logger logger;

	@Schedule(minute = "*/1", hour="*")
	public int job(){
		List<AreaEvent>areaEvents = findAreaEvents();
		
		for(AreaEvent areaEvent : areaEvents)
			sendMailAndUpdateStatus(areaEvent);
		
		return areaEvents.size();
	}
	
	private void sendMailAndUpdateStatus(AreaEvent areaEvent){
		try{
			areaSendMessage.sendMessage(areaEvent);
			updateStatus(areaEvent);
		} catch(AreaMialMessageException amme){
			logger.error(amme);
		}
	}
	
	private void updateStatus(AreaEvent areaEvent){
		areaEvent.setMailSend(false);
	}

	private List<AreaEvent> findAreaEvents(){
		List<AreaEvent>areaEvents = new ArrayList<AreaEvent>();
		
		areaEvents.addAll( areaEventNetworkRepository.findAllWhereMailSendIsTrue() );
		areaEvents.addAll( areaEventGPSRepository.findAllWhereMailSendIsTrue() );
		
		return areaEvents;
	}
	
	@AroundTimeout
	public Object logMethod(InvocationContext ic) throws Exception{
		long time = System.currentTimeMillis();
		logger.info("[AreaSendMailWorker] job has started");
		Object result = null;
		
		try{
			result = ic.proceed();
			return result;
		}finally{
			logger.info("[AreaSendMailWorker] job has ended after " 
			           + (System.currentTimeMillis() - time )
			           + "ms with sended over "
			           + result
			           + " mails");
		}

	}

}
