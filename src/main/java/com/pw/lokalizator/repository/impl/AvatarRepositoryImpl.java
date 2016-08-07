package com.pw.lokalizator.repository.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.Avatar;
import com.pw.lokalizator.repository.AvatarRepository;

@Stateless
public class AvatarRepositoryImpl implements AvatarRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Avatar create(Avatar entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public Avatar save(Avatar entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(Avatar entity) {
		em.remove(entity);
	}

	@Override
	public Avatar findById(Long id) {
		return em.find(Avatar.class, id);
	}

	@Override
	public List<Avatar> findAll() {
		return em.createQuery("SELECT a FROM Avatar", Avatar.class)
				 .getResultList();
	}

}
