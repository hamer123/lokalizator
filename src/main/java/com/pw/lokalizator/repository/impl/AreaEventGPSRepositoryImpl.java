package com.pw.lokalizator.repository.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.repository.AreaEventGPSRepository;

@Stateless
public class AreaEventGPSRepositoryImpl implements AreaEventGPSRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public AreaEventGPS create(AreaEventGPS entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public AreaEventGPS save(AreaEventGPS entity) {
		return em.merge(entity);
	}

	@Override
	public void remove(AreaEventGPS entity) {
		em.remove(entity);
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AreaEventGPS findById(Long id) {
		return em.find(AreaEventGPS.class, id);
	}

	@Override
	public List<AreaEventGPS> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AreaEventGPS> findByAreaId(long id) {
		return em.createNamedQuery("AreaEventGPS.findByAreaId", AreaEventGPS.class)
				  .setParameter("id", id)
				  .getResultList();
	}

}
