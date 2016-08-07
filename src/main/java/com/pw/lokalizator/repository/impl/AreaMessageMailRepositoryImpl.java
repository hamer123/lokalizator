package com.pw.lokalizator.repository.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.AreaMessageMail;
import com.pw.lokalizator.repository.AreaMessageMailRepository;

@Stateless
public class AreaMessageMailRepositoryImpl implements AreaMessageMailRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public AreaMessageMail create(AreaMessageMail entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public AreaMessageMail save(AreaMessageMail entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(AreaMessageMail entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AreaMessageMail findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AreaMessageMail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateAcceptById(long id, boolean accept) {
		// TODO Auto-generated method stub
		return 0;
	}

}
