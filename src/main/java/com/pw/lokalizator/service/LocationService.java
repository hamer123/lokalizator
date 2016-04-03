package com.pw.lokalizator.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
public class LocationService {
	@EJB
	LocationRepository locationRepository;
	@EJB
	UserRepository userRepository;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createLocationAndSaveCurrentLocation(Location location, long userId){
		//find user by id
		User user = userRepository.findById(userId);
		CurrentLocation cl = user.getCurrentLocation();
		//update current location
		cl.setDate(new Date());
		cl.setLatitude(location.getLatitude());
		cl.setLongitude(location.getLongitude());
		// persist location
		location.setUser(user);
		locationRepository.add(location);
	}
	
}
