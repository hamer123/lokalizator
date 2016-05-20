package com.pw.lokalizator.repository.impl;

import java.util.Collection;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.CellInfoMobile;
import com.pw.lokalizator.repository.CellInfoMobileRepository;

@Stateless
public class CellInfoMobileRepositoryImpl implements CellInfoMobileRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public CellInfoMobile add(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellInfoMobile save(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(CellInfoMobile entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CellInfoMobile findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CellInfoMobile> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellInfoMobile findByLocationId(Long id) {
		return em.createQuery("SELECT l.cellInfoMobile FROM LocationNetwork l WHERE l.id =:id", CellInfoMobile.class)
		  .setParameter("id", id)
		  .getSingleResult();
	}

}
