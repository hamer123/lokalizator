package com.pw.lokalizator.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
		return em.createNamedQuery("AreaPoint.findByPolygonModelId", AreaPoint.class)
		  .setParameter("id", id)
		  .getResultList();
	}

	@Override
	public Map<Integer, AreaPoint> findByAreaIdOrderByNumberMapToNumber(long id) {
		List<AreaPoint>areaPoints = em.createNamedQuery("AreaPoint.findByAreaIdOrderByNumber", AreaPoint.class)
				                      .setParameter("id", id)
				                      .getResultList();
		
		Map<Integer,AreaPoint>map = new HashMap<>();
		for(AreaPoint areaPoint : areaPoints)
			map.put(areaPoint.getNumber(), areaPoint);
				                                    
		return map;
	}

}
