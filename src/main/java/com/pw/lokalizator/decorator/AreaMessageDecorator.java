package com.pw.lokalizator.decorator;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.service.AreaMessageService;

//INCEPTOR AOP
@Decorator
public class AreaMessageDecorator implements AreaMessageService{
	@Inject @Delegate @Any
	private AreaMessageService AreaMessageService;
	
	private Logger logger = Logger.getLogger(AreaMessageDecorator.class);
	
	@Override
	public void sendMessage(AreaEvent areaEvent) {
		logger.info("[AreaMessageDecorator] TEST AreaMessageService sendMessage has been invoked for ");
		long time = System.currentTimeMillis();
		
		AreaMessageService.sendMessage(areaEvent);
		
		logger.info("[AreaMessageDecorator] TEST AreaMessageService sendMessage has ended after " 
		            + (System.currentTimeMillis() - time) 
		            + "ms");
	}

}
