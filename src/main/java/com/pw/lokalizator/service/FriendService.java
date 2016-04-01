package com.pw.lokalizator.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
@LocalBean
public class FriendService {
	@EJB
	private UserRepository userRepository;
	
	public void sendInvitation(User from, String login){

		try{
			
			User odbiorca = userRepository.findByLogin(login);
			
		} catch(Exception e){
			
		}
	}
}
