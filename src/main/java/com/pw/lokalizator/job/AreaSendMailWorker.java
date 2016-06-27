package com.pw.lokalizator.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;
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
	@Resource(name = "java:jboss/mail/lokalizator")
	private Session session;
	
	private Logger logger = Logger.getLogger(AreaSendMailWorker.class);
	
	@Schedule(minute = "*/1", hour="*")
	public void job(){
		long time = System.currentTimeMillis();
		logger.info("[AreaSendMailWorker] jobe has started");
		
		List<AreaEvent>areaEvents = findAreaEvents();
		
		for(AreaEvent areaEvent : areaEvents)
		{
			sendMailAndUpdateStatus(areaEvent);
		}
		
		logger.info("[AreaSendMailWorker] jobe has ended after " 
		           + (System.currentTimeMillis() - time )
		           + "ms with sended over "
		           + areaEvents.size()
		           + " mails");
	}
	
	private void sendMailAndUpdateStatus(AreaEvent areaEvent){
		try{
			sendMail(areaEvent);
			updateStatus(areaEvent);
		} catch(AddressException ae){
			logger.error("[AreaSendMailWorker] Bledny format email " + ae);
		} catch(MessagingException me){
			logger.error("[AreaSendMailWorker] Nie udalo sie wyslac maila z powodu zwiazanego z wysylaniem " + me);
		} catch(Exception e){
			logger.error("[AreaSendMailWorker] Nie udalo sie wyslac maila " + e);
		}
	}
	
	private void updateStatus(AreaEvent areaEvent){
		areaEvent.setMailSend(false);
	}
	
	private void sendMail(AreaEvent areaEvent) throws AddressException, MessagingException{
		Message message = new MimeMessage(session);
			
		String email = areaEvent.getArea().getProvider().getEmail();
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			message.setSubject( createSubject(areaEvent) );
			message.setText( areaEvent.getMessage() );
			
			Transport.send(message);

	}
	
	private List<AreaEvent> findAreaEvents(){
		List<AreaEvent>areaEvents = new ArrayList<AreaEvent>();
		
		areaEvents.addAll( areaEventNetworkRepository.findAllWhereMailSendIsTrue() );
		areaEvents.addAll( areaEventGPSRepository.findAllWhereMailSendIsTrue() );
		
		return areaEvents;
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
}
