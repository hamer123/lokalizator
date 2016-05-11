package com.pw.lokalizator.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.Roles;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.UserRepository;

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

	public User findByLoginAndPassword(String login, String password) {
		Object[] result = (Object[]) em.createNamedQuery("USER.Native.findUserByLoginAndPassword")
				 .setParameter("login", login)
				 .setParameter("password", password)
				 .getSingleResult();
		
		return new User(((BigInteger)result[0]).longValue(),
				        (String)result[1], 
				        (boolean)result[2], 
				        Roles.valueOf((String)result[3]), 
				        (String)result[4]);
	}

	public User findByLogin(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}

	public List<User> findByLoginLike(String loginLike) {
		return em.createNamedQuery("USER.findByLoginLike", User.class)
				 .setParameter("loginLike", loginLike + "%")
				 .getResultList();
	}

	@Override
	public User findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(Long id) {
		return em.createNamedQuery("USER.findByIdsGetIdAndLoginAndCurrentLocationsForAllProviders", User.class)
		  .setParameter("id", id)
		  .getSingleResult();
	}

}
