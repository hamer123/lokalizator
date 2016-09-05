package com.pw.lokalizator.rest.resource.security;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.lokalizator.model.security.Credentials;
import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.security.restful.AuthenticateService;
import com.pw.lokalizator.security.restful.DatabaseAuthenticat;
import com.pw.lokalizator.security.restful.Secured;
import org.jboss.logging.Logger;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.singleton.RestSessionManager;

@Path(LogResource.LOG_PATH)
public class LogResource {
	public static final String LOG_PATH = "/log";
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private Logger logger;
	@Inject @DatabaseAuthenticat
	private AuthenticateService authenticateService;
	
	@POST
	@Path(LOGIN)
	@Consumes( value = {MediaType.APPLICATION_FORM_URLENCODED} )
	public Response login(@BeanParam Credentials credentials){
		try{
			User user = authenticateService.authenticate(credentials.getLogin(), credentials.getPassword());
			RestSession restSession = restSessionManager.addSession(user);
			return Response.ok()
					.entity(restSession.getToken())
					.build();
		}catch(NoResultException nre){
			logger.error(nre);
			return Response.status( Response.Status.UNAUTHORIZED )
					       .entity("Konto o podanych parametrach nie istnieje !")
					       .build();
		}catch(Exception e){
			logger.error(e);
			return Response.status( Response.Status.INTERNAL_SERVER_ERROR )
					       .build();
		}
	}
	
	@Path(LOGOUT)
	@GET
	@Secured
	public Response logout(@HeaderParam("Authenticate")String token){
		try{
			token = extractToken(token);
			boolean result = restSessionManager.invalidationRestSession(token);
			if(result == true){
				return Response.status( Response.Status.OK )
						       .build();
			}
		} catch(Exception e) {
			logger.error(" Nie udane wylogowanie dla [ token: " + token + " ]" + e.getMessage());
		}
		return Response.serverError().build();
	}

	private String extractToken(String authorizationHeader){
		return authorizationHeader.substring("Bearer".length()).trim();
	}

}
