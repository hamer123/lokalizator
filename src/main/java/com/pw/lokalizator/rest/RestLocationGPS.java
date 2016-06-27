package com.pw.lokalizator.rest;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.service.LocationService;

@Path("/location/gps")
public class RestLocationGPS {
	@Inject
	private LocationService locationService;
	@Inject
	Logger logger;
	
	
	@Inject
	Event<Location>locations;
	
	@POST()
	@Path("/create")
	@Consumes( value = {MediaType.APPLICATION_JSON} )
	@Produces( value = {MediaType.APPLICATION_JSON} )
	public Response createLocation(LocationGPS locationGPS , @Context HttpServletRequest request){
		try{
			RestSession session = (RestSession)request.getSession().getAttribute( RestSession.REST_SESSION_ATR );
			long id = session.getUser().getId();
			locationService.createLocationGPSUpdateUserCurrentLocationGPS(locationGPS, id);
			
			locations.fire(locationGPS);
			
			return Response.status( Response.Status.CREATED )
					.build();
		}catch(Exception e){
			
			logger.error("[RestLocationGPS] Nie ulado sie zapisac lokalizacji..." + e);
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
	}
	
}
