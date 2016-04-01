package com.pw.lokalizator.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Friend;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.service.FriendService;

@Stateless
public class CurrentLocationRepositoryImpl implements CurrentLocationRepository{
	@PersistenceContext
	EntityManager em;
	
	Logger log = Logger.getLogger(CurrentLocationRepositoryImpl.class);
	
	@Override
	public CurrentLocation add(CurrentLocation entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrentLocation save(CurrentLocation entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(CurrentLocation entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CurrentLocation findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CurrentLocation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CurrentLocation findByUserId(long id) {
		return em.createNamedQuery("CurrentLocation.findBuUserId", CurrentLocation.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CurrentLocation> findByUsersId(Set<Long> ids) {
		return em.createNamedQuery("CurrentLocation.findByUsersId", CurrentLocation.class)
				 .setParameter("ids", ids)
				 .getResultList();
	}

}
