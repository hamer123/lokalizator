package com.pw.lokalizator.security;

import java.security.Principal;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.UserSecurity;

@Local
@Stateless
public class SecurityService {
	@EJB
	private SecurityRepository repository;
	
	private Logger log = Logger.getLogger(SecurityService.class);

	public SecurityContext createSecurityContext(String tokenKey, String securityKey, HttpServletRequest request){
		log.info("Trying to create SecurityContext using tokenKey " + tokenKey + " and securityKey " + securityKey);
		try{
			UserSecurity us = repository.findBySecurityKeyAndTokenKey(securityKey, tokenKey);
			/*
			 * Create HttpSession
			 */
			request.getSession().setAttribute("user", us.getUser());
			
			/*
			 * Create SecurityContext
			 */
			return new SecurityContext() {
				public boolean isUserInRole(String role){
					return us.getRola().toString().equalsIgnoreCase(role);
				}
				public boolean isSecure() {
					return false;
				}
				public Principal getUserPrincipal() {
					/*
					 * Create Principal
					 */
					return new Principal() { public String getName() {return us.getUser().getLogin();}};}
				
				public String getAuthenticationScheme() {
					return us.getRola().toString();
				}};
		}catch(PersistenceException pe){
			log.error("Coulndt find entity... wrong keys! " + securityKey + ", " + tokenKey);
			throw new RuntimeException(pe);
		}catch(Exception e){
			log.error("Unexpected exception");
			throw new RuntimeException(e);
		}
		
	}
}
