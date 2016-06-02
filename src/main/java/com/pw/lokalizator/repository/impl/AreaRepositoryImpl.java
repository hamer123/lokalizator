package com.pw.lokalizator.repository.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.repository.AreaRepository;

@Stateless
public class AreaRepositoryImpl implements AreaRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Area create(Area entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public Area save(Area entity) {
		return em.merge(entity);
	}

	@Override
	public void remove(Area entity) {
		em.remove(entity);
	}

	@Override
	public void remove(Long id) {
		Area area = em.find(Area.class, id);
		em.remove(area);
	}

	@Override
	public Area findById(Long id) {
		return em.find(Area.class, id);
	}

	@Override
	public List<Area> findAll() {
		return em.createNamedQuery("Area.findAll", Area.class)
				  .getResultList();
	}

	@Override
	public List<Area> findByTargetId(long id){
		return em.createNamedQuery("Area.findByTargetId", Area.class)
		         .setParameter("id", id)
		         .getResultList();
	}

	@Override
	public List<Area> findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId(long id) {
		return em.createNamedQuery("Area.findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId", Area.class)
				 .setParameter("id", id)
				 .getResultList();
	}

	@Override
	public List<Area> findWithEagerFetchPointsAndTargetByProviderId(long id) {
		List<Area>polygonModels = em.createNamedQuery("Area.findWithEagerFetchPointsAndTargetByProviderId", Area.class)
				 .setParameter("id", id)
				 .getResultList();	

		for(Area polygonModel : polygonModels)
			polygonModel.getPoints().size();
		
		return polygonModels;
	}

}
