package com.pw.lokalizator.repository.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.PolygonModel;
import com.pw.lokalizator.repository.PolygonModelRepository;

@Stateless
public class PolygonModelRepositoryImpl implements PolygonModelRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public PolygonModel add(PolygonModel entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public PolygonModel save(PolygonModel entity) {
		return em.merge(entity);
	}

	@Override
	public void remove(PolygonModel entity) {
		em.remove(entity);
		
	}

	@Override
	public void remove(Long id) {
		em.createNamedQuery("PolygonModel.removeById", PolygonModel.class)
		  .setParameter("id", id)
		  .executeUpdate();
	}

	@Override
	public PolygonModel findById(Long id) {
		return em.find(PolygonModel.class, id);
	}

	@Override
	public Collection<PolygonModel> findAll() {
		return em.createNamedQuery("PolygonModel.findAll", PolygonModel.class)
				  .getResultList();
	}

	@Override
	public List<PolygonModel> getPolygonsByTargetId(Long id){
		return em.createNamedQuery("PolygonModel.getPolygonsByTargetId", PolygonModel.class)
		         .setParameter("id", id)
		         .getResultList();
	}

}
