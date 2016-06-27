package com.pw.lokalizator.security;

import java.io.IOException;
import java.security.Principal;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.http.HttpRequest;
import org.apache.http.HttpServerConnection;
import org.jboss.logging.Logger;
import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import com.pw.lokalizator.model.entity.User;

@Provider
@PreMatching
public class RESTRequestFilter implements ContainerRequestFilter{
	@EJB
	private SecurityService securityService;
	@Context 
	private HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(RESTRequestFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {

        String path = requestCtx.getUriInfo().getPath();
		logger.info("RESt request " + requestCtx.getRequest().getMethod() + " path " + path );
          
        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if (requestCtx.getRequest().getMethod().equals( "OPTIONS" )) {
            requestCtx.abortWith(Response.status( Response.Status.OK ).build() );
            return;
        }
        
        if(path.startsWith( "/user/login" )){
        	return;
        }

       createRestSession(requestCtx);
	}
	
	private void createRestSession(ContainerRequestContext requestCtx){
		try{
	        String token = requestCtx.getHeaderString( HTTPHeaderNames.AUTH_TOKEN);
	        
	        if(validateToken(token)){
	        	SecurityContext sc = securityService.createSecurityContext ( token, request );
	        	requestCtx.setSecurityContext(sc);
	        }
		} catch(Exception e){
        	requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
        	logger.error(e);
		}
	}
	
	private boolean validateToken(String token){
		return token != null;
	}
	
}
