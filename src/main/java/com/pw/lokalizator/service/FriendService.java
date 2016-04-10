package com.pw.lokalizator.service;



import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pw.lokalizator.model.FriendInvitation;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.FriendInvitationRepository;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
@LocalBean
public class FriendService {
	@EJB
	private UserRepository userRepository;
	@EJB
	private FriendInvitationRepository friendInvitationRepository;
	
	public void sendInvitation(User from, String login){

		try{
			//Check if the user exists
			User odbiorca = userRepository.findByLogin(login);
			//Check if we arleady send the same question or we got it from opponent site
			FriendInvitation invitation = null;
			
			invitation = friendInvitationRepository.getByUsersId(from.getId(), odbiorca.getId());

			if(invitation == null){
				//We have to create FriendInvitation
				invitation = new FriendInvitation();
				invitation.setDate(new Date());
				invitation.setFrom(from);
				invitation.setTo(odbiorca);
				friendInvitationRepository.add(invitation);

			}else{
				throw new IllegalArgumentException("Zaproszenie juz istnieje");
			}
		} catch(NoResultException nre){
			throw new NoResultException("Uzytkownik nie istnieje");
		}
	}
	
	public void acceptInvitation(String sender, String reciver){
		
	}
	
	public void rejectionInvitation(){
		
	}
}
