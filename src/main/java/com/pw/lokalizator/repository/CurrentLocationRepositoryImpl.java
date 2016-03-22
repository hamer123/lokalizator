package com.pw.lokalizator.repository;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.CurrentLocation;

@Stateless
public class CurrentLocationRepositoryImpl implements CurrentLocationRepository{
	@PersistenceContext
	EntityManager em;
	
	public CurrentLocation add(CurrentLocation entity) {
		em.persist(entity);
		return entity;
	}

	public CurrentLocation save(CurrentLocation entity) {
		return em.merge(entity);
	}

	public void remove(CurrentLocation entity) {
		em.remove(entity);
	}

	public void remove(Long id) {
		int result = em.createQuery("", CurrentLocation.class)
		               .executeUpdate();
		if(result == 0)
			throw new IllegalArgumentException("nie mozna usunac CurrentLocation o id = " + id + ", brak takiej encji");
	}

	@Override
	public CurrentLocation findById(Long id) {
		return em.find(CurrentLocation.class, id);
	}

	@Override
	public Collection<CurrentLocation> findAll() {
		return null;
	}

}
