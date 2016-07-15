package com.pw.lokalizator.repository.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.repository.LocationGPSRepository;

@Stateless
public class LocationGPSRepositoryImpl implements LocationGPSRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public LocationGPS create(LocationGPS entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public LocationGPS save(LocationGPS entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(LocationGPS entity) {
		em.remove(entity);
	}

	@Override
	public LocationGPS findById(Long id) {
		return em.find(LocationGPS.class, id);
	}

	@Override
	public List<LocationGPS> findAll() {
		return em.createQuery("SELECT l FROM LocationGPS l", LocationGPS.class)
				 .getResultList();
	}

	@Override
	public List<LocationGPS> findByLoginAndDateOrderByDate(String login, Date younger, Date older, int maxResults) {
		return em.createNamedQuery("findByUserLoginAndDateYoungerThanOlderThanOrderByDateDesc", LocationGPS.class)
				.setParameter("login", login)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .setMaxResults(maxResults)
				 .getResultList();
	}

}
