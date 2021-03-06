package com.pw.lokalizator.service.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.LocationService;

@Stateless
public class LocationServiceImpl implements LocationService{
	@Inject
	private UserRepository userRepository;
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void createLocationNetworkUpdateUserCurrentLocationNetwork(LocationNetwork locationNetwork, long userId) {
		User user = userRepository.findById(userId);
		locationNetwork.setUser(user);
		em.persist(locationNetwork);
		updateUserCurrentLocationNetwork(locationNetwork, user);
	}

	@Override
	public void createLocationGPSUpdateUserCurrentLocationGPS(LocationGPS locationGPS, long userId) {
		User user = userRepository.findById(userId);
		locationGPS.setUser(user);
		em.persist(locationGPS);
		user.setLastLocationGPS(locationGPS);
	}
	
	private void updateUserCurrentLocationNetwork(LocationNetwork locationNetwork, User user){
		if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
			user.setLastLocationNetworkNaszaUsluga(locationNetwork);
		else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
			user.setLastLocationNetworObcaUsluga(locationNetwork);
	}

}
