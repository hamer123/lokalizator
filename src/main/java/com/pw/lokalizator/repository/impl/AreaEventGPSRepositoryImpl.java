package com.pw.lokalizator.repository.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

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
	public void delete(AreaEventGPS entity) {
		em.remove(entity);
		
	}

	@Override
	public AreaEventGPS findById(Long id) {
		return em.find(AreaEventGPS.class, id);
	}

	@Override
	public List<AreaEventGPS> findAll() {
		return em.createQuery("SELECT a FROM AreaEventGPS a", AreaEventGPS.class)
				 .getResultList();
	}

	@Override
	public List<AreaEventGPS> findByAreaId(long id) {
		return em.createNamedQuery("AreaEventGPS.findByAreaId", AreaEventGPS.class)
				  .setParameter("id", id)
				  .getResultList();
	}

	@Override
	public List<AreaEventGPS> findAllWhereMailSendIsTrue() {
		return em.createNamedQuery("AreaEventGPS.findAllWhereMailSendIsTrue", AreaEventGPS.class)
				 .getResultList();
	}

	@Override
	public List<AreaEventGPS> findByAreaIdAndDate(long id, Date from) {
		return em.createNamedQuery("AreaEventGPS.findByAreaIdAndDate", AreaEventGPS.class)
				 .setParameter("id", id)
				 .setParameter("from", from, TemporalType.TIMESTAMP)
				 .getResultList();
	}

}
