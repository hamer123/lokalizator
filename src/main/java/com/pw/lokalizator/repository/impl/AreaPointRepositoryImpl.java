package com.pw.lokalizator.repository.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.repository.AreaPointRepository;

@Stateless
public class AreaPointRepositoryImpl implements AreaPointRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public AreaPoint create(AreaPoint entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public AreaPoint save(AreaPoint entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(AreaPoint entity) {
		em.remove(entity);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaPoint findById(Long id) {
		return em.find(AreaPoint.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AreaPoint> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AreaPoint> findByAreaId(long id) {
		return em.createNamedQuery("PolygonPoint.findByPolygonModelId", AreaPoint.class)
		  .setParameter("id", id)
		  .getResultList();
	}

}
