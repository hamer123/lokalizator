package com.pw.lokalizator.security;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.pw.lokalizator.model.UserSecurity;

@Stateless
public class SecurityRepository {
	@PersistenceContext(unitName="lokalizator")
	private EntityManager em;
	
	private static final String FIND_BY_SECURITY_KEY_AND_TOKEN_KEY = "SELECT s FROM UserSecurity s WHERE s.serviceKey = :skey AND s.tokenKey = :tkey";
	
	public UserSecurity findBySecurityKeyAndTokenKey(String securityKey, String tokenKey){
		return (UserSecurity) em.createNamedQuery("findByTokenAndService", UserSecurity.class)
				//.createNativeQuery(FIND_BY_SECURITY_KEY_AND_TOKEN_KEY, UserSecurity.class)
				                .setParameter("skey", securityKey)
				                .setParameter("tkey", tokenKey)
				                .getSingleResult();
	}
}
