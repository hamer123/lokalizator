package com.pw.lokalizator.service.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;

import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.serivce.qualifier.AreaMailMessage;
import com.pw.lokalizator.service.AreaMessageService;

@AreaMailMessage 
@Stateless(mappedName = "areaMailMessageService", name = "areaMailMessageService")
public class AreaMailMessageService implements AreaMessageService{
	@Resource(name = "java:jboss/mail/lokalizator")
	private Session session;
	
	@Override
	public void sendMessage(AreaEvent areaEvent) {
		User target = areaEvent.getArea().getTarget();
		
	}

}
 