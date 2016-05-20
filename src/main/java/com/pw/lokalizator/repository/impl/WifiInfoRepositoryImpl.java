package com.pw.lokalizator.repository.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;


import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.WifiInfo;
import com.pw.lokalizator.repository.WifiInfoRepository;

@Stateless
public class WifiInfoRepositoryImpl implements WifiInfoRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public WifiInfo add(WifiInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WifiInfo save(WifiInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(WifiInfo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WifiInfo findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<WifiInfo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WifiInfo findByLocationId(Long id) {
		List<WifiInfo>result =  em.createNamedQuery("WifiInfo.findByLocationId", WifiInfo.class)
				 .setParameter("id", id)
				 .getResultList();
		if(!result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}
}
