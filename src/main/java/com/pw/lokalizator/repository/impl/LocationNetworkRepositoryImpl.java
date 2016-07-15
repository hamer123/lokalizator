package com.pw.lokalizator.repository.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.repository.LocationNetworkRepository;

@Stateless
public class LocationNetworkRepositoryImpl implements LocationNetworkRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public LocationNetwork create(LocationNetwork entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public LocationNetwork save(LocationNetwork entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(LocationNetwork entity) {
		em.remove(entity);
	}

	@Override
	public LocationNetwork findById(Long id) {
		return em.find(LocationNetwork.class, id);
	}

	@Override
	public List<LocationNetwork> findAll() {
		return em.createQuery("SLECT l FROM LocationNetwork l", LocationNetwork.class)
				 .getResultList();		
	}

	@Override
	public List<LocationNetwork> findByLoginAndDateForServiceNaszOrderByDate(String login, Date younger, Date older, int maxResults) {
		return em.createNamedQuery("findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsNaszOrderByDateDesc", LocationNetwork.class)
				 .setParameter("login", login)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .setMaxResults(maxResults)
				 .getResultList();
	}

	@Override
	public List<LocationNetwork> findByLoginAndDateForServiceObcyOrderByDate(String login, Date younger, Date older, int maxResults) {
		return em.createNamedQuery("findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsObcyOrderByDateDesc", LocationNetwork.class)
				 .setParameter("login", login)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .setMaxResults(maxResults)
				 .getResultList();
	}

}
