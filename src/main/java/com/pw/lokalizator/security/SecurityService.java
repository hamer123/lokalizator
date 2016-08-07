package com.pw.lokalizator.security;

import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.singleton.RestSessionManager;

@Local
@Stateless
public class SecurityService {
	@Inject
	private UserRepository userRepository;
	@EJB
	private RestSessionManager restSessionManager;
	
	private static final SecureRandom random = new SecureRandom();
	private Logger logger = Logger.getLogger(SecurityService.class);


	public SecurityContext createSecurityContext(String token, HttpServletRequest request){
		try{
			logger.info("Trying to create SecurityContext [ token : " + token + " ]");
			
			RestSession session = restSessionManager.getRestSession(token);
			session.setLastUsed(new Date());
			request.getSession().setAttribute(RestSession.REST_SESSION_ATR , session);
			return new SecurityContextRest( session.getUser() );
			
		}catch(PersistenceException pe){
			logger.error("[SecurityService] Couldnt find RestSession... wrong token! " + token);
			throw new RuntimeException(pe);
		}catch(Exception e){
			logger.error("[SecurityService] Unexpected exception");
			throw new RuntimeException(e);
		}
	}
	
	public String createRestSessionReturnToken(String login, String password){
		User user = userRepository.findUserFeatchDefaultSettingByLoginAndPassword(login, password);
		String token = restSessionManager.getTokenForLogin(login);
		
		if(token == null){
			token = generateToken(user.getId());
			restSessionManager.addRestSession(token, user);
			return token;
		}
		
		return token;
	}
	
	
	private String generateToken(long id){
		String token = null;
		token = new BigInteger(130, random).toString(32) + id;
		return token;
	}
	
	public boolean logout(String token){
		return restSessionManager.invalidationRestSession(token);
	}
	
	
	/**
	 * 
	 * @author Patryk
	 *
	 */
	private class SecurityContextRest implements SecurityContext{

		private User user;
		
		public SecurityContextRest(User user){
			this.user = user;
		}
		
		@Override
		public Principal getUserPrincipal() {
			return new Principal() { 
				public String getName() {
					return user.getLogin();
				}
			};
		}

		@Override
		public boolean isUserInRole(String role) {
			return user.getRola()
			           .toString()
			           .equalsIgnoreCase(role);
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public String getAuthenticationScheme() {
			return user.getRola()
			           .toString();
		}
		
	}
}
