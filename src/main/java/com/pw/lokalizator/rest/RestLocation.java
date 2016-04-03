package com.pw.lokalizator.rest;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import com.pw.lokalizator.model.Location;
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
			log.info("DO ZAPISANIA NOWA LOKACJA DLA " + session.getUser().getLogin() + " [ " + location.getLatitude() + ", " + location.getLongitude() + " ]");
			
			//Merge user
			User user = session.getUser();
			user.setDate( new Date() );
			user.setLatitude( location.getLatitude() );
			user.setLongitude( location.getLongitude() );
			session.setUser( userRepository.save(user) );
			
			//Persist location
			location.setDate( new Date() );
			location.setUser(user);
			locationRepository.add( location );
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
		
		return Response.status( Response.Status.CREATED )
				.build();
	}
}
