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

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Roles;
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
		em.createNamedQuery("USER.deleteByID")
		   .setParameter("id", id)
		   .executeUpdate();
	}
	
	public User findById(Long id) {
		return em.find(User.class, id);
	}
	
	public Collection<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				 .getResultList();
	}

	public User findUserWithSecurityByLoginAndPassword(String login, String password) {
		return em.createNamedQuery("USER.findUserWithSecurityByLoginAndPassword", User.class)
		         .setParameter("login", login)
		         .setParameter("password", password)
		         .getSingleResult();
	}

	public User findByLogin(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}

	public List<String> findLoginByLoginLike(String loginLike) {
		return em.createNamedQuery("USER.findLoginByLoginLike", String.class)
				 .setParameter("login", loginLike + "%")
				 .getResultList();
	}

	@Override
	public User findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(Long id) {
		return em.createNamedQuery("USER.findByIdsGetIdAndLoginAndCurrentLocationsForAllProviders", User.class)
		  .setParameter("id", id)
		  .getSingleResult();
	}

	@Override
	public User findUserWithPolygonsByLogin(String login) {
		User user =  em.createNamedQuery("USER.findByLogin", User.class)
				       .setParameter("login", login)
				       .getSingleResult();
		user.getPolygons().size();
		return user;
	}

}
