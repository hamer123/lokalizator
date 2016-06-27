package com.pw.lokalizator.service.inceptor;

import java.util.Date;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.RestSession;

public class RestSessionLastUsedInceptor {
	
	@AroundInvoke
	public Object changeLastUsedDate(InvocationContext ctx) throws Exception{
		Object result = ctx.proceed();
		setRestSessionLastUsedDate(result);
		return result;
	}
	
	private void setRestSessionLastUsedDate(Object object){
		if(validateObject(object)){
			RestSession restSession = (RestSession)object;
			restSession.setLastUsed(new Date());
		}
	}
	
	private boolean validateObject(Object object){
		return  object != null &&
				object instanceof RestSession;
	}
}
