package com.pw.lokalizator.repository;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.pw.lokalizator.model.Friend;




@Stateless
public class FriendRepositoryImpl implements FriendRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Friend add(Friend entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Friend save(Friend entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Friend entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Friend findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Friend> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Friend> findByUserId(Long id) {
		return em.createNamedQuery("Friend.finyByUserId", Friend.class)
				 .setParameter("id", id)
				 .getResultList();
	}
}
