package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.User;

@Stateless
public class UserDao implements Dao<User>{
	
	@PersistenceContext(unitName = "lokalizator")
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(User e) {
		em.persist(e);
	}

	public User merge(User e) {
		return em.merge(e);
	}

	@TransactionAttribute()
	public User findById(long id) {
		return em.find(User.class, id);
	}

	public List<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				.getResultList();
	}

}
