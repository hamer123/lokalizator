package com.pw.lokalizator.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

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

	public void remove(Long id) {
		int result = em.createNamedQuery("Location.deleteById", Location.class).executeUpdate();
		if(result == 0)
			throw new IllegalArgumentException("nie mozna usunac Location o id = " + id + ", brak takiej encji");
	}

	public Location findById(Long id) {
		return em.find(Location.class, id);
	}

	public Collection<Location> findAll() {
		return em.createQuery("SELECT l FROM Location l", Location.class).getResultList();
	}

	public long count() {
		return em.createNamedQuery("Location.count").getFirstResult();
	}

	public List<Location> getLocationYoungThanAndOlderThan(Date younger, Date older) {
		return em.createNamedQuery("Location.findOlderThanAndYoungerThan", Location.class)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .getResultList();
	}

}
