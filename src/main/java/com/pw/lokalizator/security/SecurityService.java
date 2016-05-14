package com.pw.lokalizator.security;

import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.model.UserSecurity;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.singleton.RestSessionManager;

@Local
@Stateless
public class SecurityService {
	@Inject
	private UserRepository userRepository;
	@EJB
	private RestSessionManager restSessionSimulator;
	private static final SecureRandom random = new SecureRandom();
	
	private Logger log = Logger.getLogger(SecurityService.class);

	/*
	 * Create SecurityContext with validate serviceKey and authToken and bind RestSession to HttpSession
	 */
	public SecurityContext createSecurityContext(String serviceKey, String authToken, HttpServletRequest request){
		
		log.info("Trying to create SecurityContext [ service : " + serviceKey + " ] [ token : " + authToken + " ]");
		
		try{
			
			RestSession session = restSessionSimulator.getRestSession(serviceKey);
			
			if(session == null || !session.getAuthToken().equals(authToken)){
				throw new SecurityException("Nie poprawny token lub service");
			}
			request.getSession().setAttribute(RestSession.REST_SESSION_ATR , session);

			return new SecurityContext() {
				
				public boolean isUserInRole(String role){
					return session.getUser()
							.getUserSecurity()
							.getRola()
							.toString()
							.equalsIgnoreCase(role);
				}
				
				public boolean isSecure() {
					return false;
				}
				
				public Principal getUserPrincipal() { 
					return new Principal() { 
						public String getName() {
							return session.getUser().getLogin();
						}
					};
			    }
				
				public String getAuthenticationScheme() {
					return session.getUser()
							.getUserSecurity()
							.getRola()
							.toString();
				}
			};
		}catch(PersistenceException pe){
			log.error("Coulndt find RestSession... wrong key! " + serviceKey);
			throw new RuntimeException(pe);
		}catch(Exception e){
			log.error("Unexpected exception");
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * Validate login request, create RestSession
	 */
	public RestSession validateRestLogin(String login, String password){
		User user = null;
		
		try{
			user = userRepository.findUserWithSecurityByLoginAndPassword(login, password);
		} catch(Exception e){
			log.warn("Nie udana pruba logowania dla " + login);
			throw new RuntimeException(e);
		}
		
		try{
			return restSessionSimulator.createRestSession(user.getUserSecurity().getServiceKey(), 
                                                   generateAuthToken(),
                                                   user);
		}catch(Exception e){
			log.info("Blad przy tworzeniu RestSession dla uzytkowniaka " + login);
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * Generate authToken
	 */
	private String generateAuthToken(){
		return new BigInteger(130, random).toString(32);
	}
	
	/*
	 * logout and invalidate RestSession
	 */
	
	public boolean logout(String serviceKey){
		return restSessionSimulator.invalidationRestSession(serviceKey);
	}
}
