package com.pw.lokalizator.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.RestSecurityKeys;
import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.Role;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.model.UserSecurity;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.security.HTTPHeaderNames;
import com.pw.lokalizator.security.SecurityService;

@Path("/user")
public class RestUser {
	@EJB
	private SecurityService securityService;
	Logger log = Logger.getLogger(RestUser.class);
	
	@Path("/login")
	@POST
	@Consumes( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response login(User user){
		try{
			RestSession session = securityService.validateRestLogin(user.getLogin(), user.getPassword());
			
			return Response.status( Response.Status.OK )
					       .entity( new RestSecurityKeys( session.getUser().getUserSecurity().getServiceKey(), session.getAuthToken() ))
					       .build();
		}catch(Exception e){
			//TODO
			e.printStackTrace();
			
			return Response.status( Response.Status.EXPECTATION_FAILED )
					       .build();
		}
		
	}
	
	@Path("/logout")
	@GET
	public Response logout( @HeaderParam( HTTPHeaderNames.SERVICE_KEY )String serviceKey ){
		try{
			boolean result = securityService.logout(serviceKey);
			if(result == true){
				return Response.status( Response.Status.OK )
						       .build();
			}
		}catch(Exception e){
			//TODO
			e.printStackTrace();
		}
		
		return Response.status( Response.Status.EXPECTATION_FAILED )
				       .build();
	}
	
}
