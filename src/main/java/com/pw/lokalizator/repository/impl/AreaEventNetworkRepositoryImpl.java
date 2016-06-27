package com.pw.lokalizator.repository.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;

@Stateless
public class AreaEventNetworkRepositoryImpl implements AreaEventNetworkRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public AreaEventNetwork create(AreaEventNetwork entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public AreaEventNetwork save(AreaEventNetwork entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(AreaEventNetwork entity) {
		em.remove(entity);
	}

	@Override
	public void delete(Long id) {
		em.createNamedQuery("AreaEventNetwork.removeById")
		  .executeUpdate();
	}

	@Override
	public AreaEventNetwork findById(Long id) {
		return em.find(AreaEventNetwork.class, id);
	}

	@Override
	public List<AreaEventNetwork> findAll() {
		return em.createNamedQuery("AreaEventNetwork.findAll", AreaEventNetwork.class)
				 .getResultList();
	}

	@Override
	public List<AreaEventNetwork> findByAreaId(long id) {
		return em.createNamedQuery("AreaEventNetwork.findByAreaId", AreaEventNetwork.class)
				  .setParameter("id", id)
				  .getResultList();
	}

	@Override
	public List<AreaEventNetwork> findAllWhereMailSendIsTrue() {
		return em.createNamedQuery("AreaEventNetwork.findAllWhereMailSendIsTrue", AreaEventNetwork.class)
				 .getResultList();
	}

}
