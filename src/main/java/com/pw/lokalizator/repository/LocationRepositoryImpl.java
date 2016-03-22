package com.pw.lokalizator.repository;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.Location;

@Stateless
public class LocationRepositoryImpl implements LocationRepository{
	@PersistenceContext
	EntityManager em;
	
	public Location add(Location entity) {
		em.persist(entity);
		return entity;
	}

	public Location save(Location entity) {
		return em.merge(entity);
	}

	public void remove(Location entity) {
		em.remove(entity);
	}

	@Override
	public void remove(Long id) {
		int result = em.createNamedQuery("Location.deleteById", Location.class).executeUpdate();
		if(result == 0)
			throw new IllegalArgumentException("nie mozna usunac Location o id = " + id + ", brak takiej encji");
	}

	@Override
	public Location findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Location> findAll() {
		return em.createNamedQuery("", Location.class).getResultList();
	}

	public long count() {
		return em.createNamedQuery("Location.count").getFirstResult();
	}

}
