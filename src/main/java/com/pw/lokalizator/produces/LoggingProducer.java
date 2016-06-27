package com.pw.lokalizator.produces;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

public class LoggingProducer {

	@Produces
	private Logger createLogger(InjectionPoint injectionPoint){
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}
