package com.pw.lokalizator.repository.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
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

	public User create(User entity) {
		em.persist(entity);
		return entity;
	}
	
	public User save(User entity) {
		return em.merge(entity);
	}
	
	public void delete(User entity) {
		em.remove(entity);
	}
	
	public int delete(Long id) {
		return em.createNamedQuery("USER.deleteByID")
		         .setParameter("id", id)
		         .executeUpdate();
	}
	
	public User findById(Long id) {
		return em.find(User.class, id);
	}
	
	public List<User> findAll() {
		return em.createNamedQuery("USER.findAll", User.class)
				 .getResultList();
	}

	public User findUserFeatchDefaultSettingByLoginAndPassword(String login, String password) {
		return em.createNamedQuery("USER.findUserFeatchDefaultSettingByLoginAndPassword", User.class)
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
		user.getAreas().size();
		return user;
	}

	@Override
	public List<User> findById(Set<Long> id) {
		return em.createNamedQuery("USER.findUsersById", User.class)
		         .setParameter("id", id)
		         .getResultList();
	}

	@Override
	public List<User> findByIdFetchEagerLastLocations(Set<Long> id) {
		return em.createQuery("SELECT u FROM User u LEFT OUTER JOIN FETCH u.lastLocationNetworkNaszaUsluga LEFT OUTER JOIN FETCH u.lastLocationNetworObcaUsluga LEFT OUTER JOIN FETCH u.lastLocationGPS WHERE u.id IN (:id)", User.class)
				 .setParameter("id", id)
				 .getResultList();
	}

	@Override
	public User findByIdFetchEagerLastLocations(String login){
		List<User>results =  em.createNamedQuery("USER.findByIdFetchEagerLastLocations", User.class)
				               .setParameter("login", login)
				               .getResultList();

        if (results.isEmpty())
        	return null;
        else if (results.size() == 1)
        	return results.get(0);
        
        throw new NonUniqueResultException();	
	}

	@Override
	public List<User> findByLogin(List<String> logins) {
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
	public User findByEmail(String email) {
		return em.createNamedQuery("USER.findByEmail", User.class)
				 .setParameter("email", email)
				 .getSingleResult();
	}
}
