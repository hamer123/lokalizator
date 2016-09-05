package com.pw.lokalizator.repository.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
public class UserRepositoryImpl implements UserRepository{
	@PersistenceContext
	private EntityManager em;

	@Override
	public User create(User entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public User save(User entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(User entity) {
		em.remove(entity);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User findById(Long id) {
		return em.find(User.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				 .getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User findUserFeatchRolesByLoginAndPassword(String login, String password) {
		return em.createNamedQuery("USER.findUserFetchRolesByLoginAndPassword", User.class)
		         .setParameter("login", login)
		         .setParameter("password", password)
		         .getSingleResult();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User findByLogins(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> findLoginByLoginLike(String loginLike) {
		return em.createNamedQuery("USER.findLoginByLoginLike", String.class)
				 .setParameter("login", loginLike + "%")
				 .getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<User> findByIds(Set<Long> ids) {
		return em.createNamedQuery("USER.findUsersByIds", User.class)
		         .setParameter("ids", ids)
		         .getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<User> findByLogins(List<String> logins) {
		return em.createNamedQuery("USER.findByLogins", User.class)
				 .setParameter("logins", logins)
				 .getResultList();
	}

	@Override
	public User findByLoginFetchArea(String login) {
		User user =  em.createNamedQuery("USER.findByLogin", User.class)
			       .setParameter("login", login)
			       .getSingleResult();
		
		List<Area>areas = user.getAreas();
		for(Area area : areas)
			area.getPoints().size();
		
		return user;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User findByEmail(String email) {
		return em.createNamedQuery("USER.findByEmail", User.class)
				 .setParameter("email", email)
				 .getSingleResult();
	}
}
