package com.pw.lokalizator.repository.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.PolygonPoint;
import com.pw.lokalizator.repository.PolygonPointRepository;

@Stateless
public class PolygonPointRepositoryImpl implements PolygonPointRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public PolygonPoint create(PolygonPoint entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public PolygonPoint save(PolygonPoint entity) {
		return em.merge(entity);
	}

	@Override
	public void remove(PolygonPoint entity) {
		em.remove(entity);
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PolygonPoint findById(Long id) {
		return em.find(PolygonPoint.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PolygonPoint> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PolygonPoint> findByPolygonModelId(long id) {
		return em.createNamedQuery("PolygonPoint.findByPolygonModelId", PolygonPoint.class)
		  .setParameter("id", id)
		  .getResultList();
	}

}
