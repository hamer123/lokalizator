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
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Roles;
import com.pw.lokalizator.repository.UserRepository;

@Stateless
public class UserRepositoryImpl implements UserRepository{
	@PersistenceContext
	private EntityManager em;
	
//	private static final String USER_updateLastLocationNetworkNaszaUslugaById = "UPDATE User u SET u.lastLocationNetworkNaszaUsluga =:location WHERE u.id =:userId";
	private static final String USER_updateLastLocationNetworkObcaUslugaById  = "UPDATE User u SET u.lastLocationNetworObcaUsluga =:location WHERE u.id =:userId";
	private static final String USER_updateLastLocationGPSById                = "UPDATE User u SET u.lastLocationGPS =:location WHERE u.id =:userId";

	
	
	public User create(User entity) {
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

	@Override
	public List<User> findById(Set<Long> id) {
		return em.createNamedQuery("USER.findUsersById", User.class)
		         .setParameter("id", id)
		         .getResultList();
	}

	@Override
	public int updateCurrentLocationNetworkNaszaUsluga(long locationId, long userId) {
		return em.createNamedQuery("User.Native.updateUserLastLocationNetworkNaszaUslugaById")
				 .setParameter("locationId", locationId)
				 .setParameter("userId", userId)
				 .executeUpdate();
	}

	@Override
	public int updateCurrentLocationNetworkObcaUsluga(long locationId, long userId) {
		return em.createQuery(USER_updateLastLocationNetworkObcaUslugaById)
				 .setParameter("locationId", locationId)
				 .setParameter("userId", userId)
				 .executeUpdate();
	}

	@Override
	public int updateCurrentLocationGPS(long locationId, long userId) {
		return em.createQuery(USER_updateLastLocationGPSById)
				 .setParameter("locationId", locationId)
				 .setParameter("userId", userId)
				 .executeUpdate();
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
}
