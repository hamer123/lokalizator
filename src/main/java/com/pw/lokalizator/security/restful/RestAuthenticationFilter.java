package com.pw.lokalizator.security.restful;


import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.rest.resource.RestAttribute;
import com.pw.lokalizator.singleton.RestSessionManager;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;


@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class RestAuthenticationFilter implements ContainerRequestFilter{
	@Inject
	private RestSessionManager restSessionManager;
	@Context 
	private HttpServletRequest request;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
			throw new NotAuthorizedException("Authorization header must be provided");

		//Check if there is token in hashmap (validate)
		String token = extractToken(authorizationHeader);
		RestSession restSession = restSessionManager.getSession(token);
		if(restSession == null){
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
		} else {
			request.setAttribute(RestAttribute.REST_SESSION, restSession);
			ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
			factory.pushContext(RestSession.class, restSession);
		}
	}

	private String extractToken(String authorizationHeader){
		return authorizationHeader.substring("Bearer".length()).trim();
	}
}
