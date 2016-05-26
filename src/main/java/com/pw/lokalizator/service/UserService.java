package com.pw.lokalizator.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Roles;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
public class UserService implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	UserRepository userRepository;
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createAcount(User user){
		
		user.setRola(Roles.USER);
		
		userRepository.create(user);
		
	}
}
