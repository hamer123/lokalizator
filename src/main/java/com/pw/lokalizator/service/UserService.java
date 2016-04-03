package com.pw.lokalizator.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Role;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.model.UserSecurity;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
public class UserService implements Serializable{
	private static final long serialVersionUID = 1L;
	@EJB
	UserRepository userRepository;
	private SecureRandom random; 
	
	public void createAccount(User user){
		
		CurrentLocation currentLocation = new CurrentLocation();
		currentLocation.setLatitude(51.60604194516316);
		currentLocation.setLongitude(18.937854766845703);
		currentLocation.setDate(new Date());
		currentLocation.setUser(user);
		
		UserSecurity security = new UserSecurity();
		security.setRola(Role.USER);
		security.setServiceKey(serviceKeyGenerator(user.getLogin().hashCode()));
		security.setUser(user);
		
		user.setUserSecurity(security);
		user.setCurrentLocation(currentLocation);
		user.setEnable(true);
		
		userRepository.add(user);
	}
	
	public String serviceKeyGenerator(int hash){
		random = new SecureRandom();
		return new BigInteger(130, random).toString(32) + Math.abs(hash);
	}
}
