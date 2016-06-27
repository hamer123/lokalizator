package com.pw.lokalizator.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.UserLastLocationsService;

@Stateless
public class UserLastLocationsServiceImpl implements UserLastLocationsService{
	@Inject
	private UserRepository userRepository;
	
	@Override
	public void updateUserLastLocation(User user) {
		User _user = userRepository.findById(user.getId());
		
		user.setLastLocationGPS(_user.getLastLocationGPS());
		user.setLastLocationNetworkNaszaUsluga(_user.getLastLocationNetworkNaszaUsluga());
		user.setLastLocationNetworObcaUsluga(_user.getLastLocationNetworObcaUsluga());
	}

}
