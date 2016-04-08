package com.pw.lokalizator.rest;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.ProviderType;
import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.UserRepository;


@Path("/location")
public class RestLocation {
	@EJB
	UserRepository userRepository;
	@EJB
	LocationRepository locationRepository;
	
	Logger log = Logger.getLogger(RestLocation.class);
	
	@Path("/create")
	@POST
	@Consumes
	public Response createLocation(Location location, @Context HttpServletRequest request){
		RestSession session = (RestSession)request.getSession().getAttribute( RestSession.REST_SESSION_ATR );
		
		try{
			log.info("zapisywanie lokalizacji dla " + session.getUser().getLogin() 
					+ "  [ " + location.getLatitude() + ", " + location.getLongitude() + " ] " + location.getDate() 
					+ " : " + location.getProvider());
			
			User user = userRepository.findById( session.getUser().getId() );
			//zapisanie nowej lokalizacji
			location.setUser(user);
			location = locationRepository.add( location );
			
			//zaktualizowanie obecnej pozycji uzytkownika
			if(location.getProvider() == ProviderType.GPS){
				user.setLastGpsLocation(location);
			}else if(location.getProvider() == ProviderType.NETWORK){
				user.setLastNetworkLocation(location);
			}else if(location.getProvider() == ProviderType.OWN){
				user.setLastOwnProviderLocation(location);
			}
			
			session.setUser( userRepository.save(user) );
			
		}catch(Exception e){
			
			log.error("nie ulado sie zapisac lokalizacji..." + e);
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
		
		return Response.status( Response.Status.CREATED )
				.build();
	}
	
	@GET
	@Path("/test")
	public String test(){
		return "test :)_";
	}
}
