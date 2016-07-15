package com.pw.lokalizator.service.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;

import com.pw.lokalizator.exception.AreaMialMessageException;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.serivce.qualifier.AreaMailMessage;
import com.pw.lokalizator.service.AreaMessageService;

@AreaMailMessage 
@Stateless
public class AreaMailMessageService implements AreaMessageService{
	@Resource(name = "java:jboss/mail/lokalizator")
	private Session session;
	
	@Override
	public void sendMessage(AreaEvent areaEvent) {
		try {
			sendMail(areaEvent);
		} catch (MessagingException e) {
			throw new AreaMialMessageException(e);
		}
	}
	
	private String createSubject(AreaEvent areaEvent){
		StringBuilder builder = new StringBuilder();
		builder.append("Uzytkownik ")
		       .append(areaEvent.getArea().getTarget().getLogin())
		       .append( getSubjectPartFollowType(areaEvent.getArea()) );
		
		return builder.toString();
	}
	
	private String getSubjectPartFollowType(Area area){
		if(area.getAreaFollowType() == AreaFollows.INSIDE)
			return " opuscil obszar sledzenia "
			       + area.getName();
		else
			return " wszedl do obszaru sledzenia "
		           + area.getName();
	}
	
	private String createMsg(AreaEvent areaEvent){
		return   areaEvent.getMessage()
		       + "\n"
		       + areaEvent.getUrl();
	}
	
	private void sendMail(AreaEvent areaEvent) throws AddressException, MessagingException{
		Message message = new MimeMessage(session);
			
		String email = areaEvent.getArea().getProvider().getEmail();
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			message.setSubject( createSubject(areaEvent) );
			message.setText( createMsg(areaEvent) );
			
			Transport.send(message);

	}

}
 