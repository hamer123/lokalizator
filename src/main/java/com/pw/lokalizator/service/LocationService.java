package com.pw.lokalizator.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.CurrentLocationRepository;
import com.pw.lokalizator.repository.LocationRepository;

@Stateless
public class LocationService {

	@EJB
	CurrentLocationRepository currentLocationRepository;
	@EJB
	LocationRepository locationRepository;
	@PersistenceContext
	private EntityManager em;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createLocationAndSaveCurrentLocation(Location location, CurrentLocation currentLocation, long userId){

		System.err.println("LOCATION_SERVICE");
		User user = em.find(User.class, userId);
		user.setCurrentLocation(currentLocation);
		location.setUser(user);
		em.persist(location);
	}
}
