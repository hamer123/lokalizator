package com.pw.lokalizator.rest;

import java.util.Date;

import javax.ejb.EJB;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.entity.Address;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.security.HTTPHeaderNames;
import com.pw.lokalizator.security.SecurityService;

@Path("/user")
public class RestUser {
	@EJB
	private SecurityService securityService;
	Logger logger = Logger.getLogger(RestUser.class);
	
	
	@POST
	@Path("/login")
	@Consumes( value = {MediaType.APPLICATION_JSON} )
	@Produces( value = {MediaType.APPLICATION_JSON} )
	public Response login(User user){
		try{
			String token = securityService.createRestSessionReturnToken(user.getLogin(), user.getPassword());
			return Response.status( Response.Status.OK )
					       .entity( token )
					       .build();
		}catch(NoResultException nre){
			logger.error("[RestUser] Nie udane logowanie [ login: " + user.getLogin() + ", password: " + user.getPassword() + " ]" + nre.getMessage());
			return Response.status( Response.Status.EXPECTATION_FAILED )
					       .entity("Konto o podanych parametrach nie istnieje !")
					       .build();
		}catch(Exception e){
			logger.error("[RestUser] Nie udane logowanie [ login: " + user.getLogin() + ", password: " + user.getPassword() + " ]" + e.getMessage());
			return Response.status( Response.Status.EXPECTATION_FAILED )
					       .build();
		}
		
	}
	
	@Path("/logout")
	@GET
	public Response logout( @HeaderParam( HTTPHeaderNames.AUTH_TOKEN )String token ){
		try{
			boolean result = securityService.logout(token);
			if(result == true){
				return Response.status( Response.Status.OK )
						       .build();
			}
		} catch(Exception e) {
			logger.error("[RestUser] Nie udane wylogowanie dla [ token: " + token + " ]" + e.getMessage());
		}
		
		return Response.status( Response.Status.EXPECTATION_FAILED )
			       .build();
	}
	
	@Path("/test")
	@GET
	@Produces( value = {MediaType.APPLICATION_JSON} )
	public Response test(){
		Location location = new LocationGPS();
		Address address = new Address();
		address.setCity("ZD");
		address.setStreet("qwe");
		location.setAddress(address);
		location.setProviderType(Providers.GPS);
		location.setDate(new Date());
		
		return Response.status( Response.Status.OK )
				       .entity(location)
				       .build();
	}
	
}
