package com.pw.lokalizator.security;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.jboss.logging.Logger;


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
            requestCtx.abortWith(Response
            		.status( Response.Status.OK )
//            		.header("Access-Control-Allow-Origin", "http://localhost:63342/untitled")
//            		.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
//            		.header("Access-Control-Allow-Headers", "X-PINGOTHER, Content-Type")
            		.build() 
            		);
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
