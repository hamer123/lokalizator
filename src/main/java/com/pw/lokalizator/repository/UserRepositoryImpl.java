package com.pw.lokalizator.repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.Role;
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
		Object[] result = (Object[]) em.createNamedQuery("USER.Native.findUserByLoginAndPassword")
				 .setParameter("login", login)
				 .setParameter("password", password)
				 .getSingleResult();
		
		return new User(((BigInteger)result[0]).longValue(),
				        (String)result[1], 
				        (boolean)result[2], 
				        Role.valueOf((String)result[3]), 
				        (String)result[4]);
		
		/*
		return em.createNamedQuery("USER.findByLoginAndPassword", User.class)
				 .setParameter("login", login)
				 .setParameter("password", password)
				 .getSingleResult();
		*/
	}

	@Override
	public User findByLogin(String login) {
		return em.createNamedQuery("USER.findByLogin", User.class)
				 .setParameter("login", login)
				 .getSingleResult();
	}

	@Override
	public List<User> findFriendsById(long id) {
		List<User>friends = null;
		
		try{
			
			friends = em.find(User.class, id).getFriends();
			friends.size();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return friends;
	}

	@Override
	public int persistFriend(long reciverId, long senderId) {
		return em.createNamedQuery("User.Native.createFriend")
				 .setParameter("user_id", reciverId)
				 .setParameter("friends_id", senderId)
				 .executeUpdate();
	}

}
