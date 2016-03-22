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

import com.pw.lokalizator.model.User;

@Provider
@PreMatching
public class RESTRequestFilter implements ContainerRequestFilter{
	@EJB
	private SecurityService securityService;
	@Context 
	private HttpServletRequest request;
	
	private Logger log = Logger.getLogger(RESTRequestFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {

        String path = requestCtx.getUriInfo().getPath();
        log.info( "Filtering request path: " + path );
        log.info( "Is HttpServletRequest null? " + request);
          
        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            requestCtx.abortWith(Response.status( Response.Status.OK ).build() );
 
            return;
        }
        
        log.info("are they true? " + path.startsWith("/user/create"));
        
        // Then check are tokens exist and are valid.    
        if( !(path.startsWith("/user/login") || path.startsWith("/user/create"))){
            String serviceKey = requestCtx.getHeaderString( HTTPHeaderNames.SERVICE_KEY );
            String authToken = requestCtx.getHeaderString( HTTPHeaderNames.AUTH_TOKEN );
            
            if(authToken != null && serviceKey != null){
                try{
                	SecurityContext sc = securityService.createSecurityContext(authToken , serviceKey, request);
                	requestCtx.setSecurityContext(sc);
                }catch(Exception e){
                	requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
                	log.error(e);
                }
            }else{
            	requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }
        
		log.info("INFO FROM REST REQUEST FILTER -> " + " REQUEST -> " + requestCtx.getRequest().getMethod() + " PATH-> " + path );
	}

}
