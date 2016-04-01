package com.pw.lokalizator.repository;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.User;

@Stateless
public class UserRepositoryImpl implements UserRepository{
	@PersistenceContext
	private EntityManager em;
	
	public User add(User entity) {
		em.persist(entity);
		return entity;
	}
	
	public User save(User entity) {
		return em.merge(entity);
	}
	
	public void remove(User entity) {
		em.remove(entity);
	}
	
	public void remove(Long id) {
		int result = em.createNamedQuery("USER.deleteByID")
		   .setParameter("id", id)
		   .executeUpdate();
		if(result == 0)
		  throw new IllegalArgumentException("nie mozna usunac User o id = " + id + ", brak takiej encji");
	}
	
	public User findById(Long id) {
		return em.find(User.class, id);
	}

	public Collection<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				 .getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public User findByLoginAndPassword(String login, String password) {
		return em.createNamedQuery("USER.findByLoginAndPassword", User.class)
				 .setParameter("login", login)
				 .setParameter("password", password)
				 .getSingleResult();
	}

	@Override
	public User findByLogin(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}
}
