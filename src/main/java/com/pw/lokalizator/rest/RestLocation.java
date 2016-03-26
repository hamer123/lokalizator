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

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.RestSession;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.service.LocationService;
import com.pw.lokalizator.singleton.RestSessionSimulator;

@Path("/location")
public class RestLocation {
	@EJB
	private LocationService locationService;
	Logger log = Logger.getLogger(RestLocation.class);
	
	@Path("/create")
	@POST
	@Consumes
	public Response createLocation(Location location, @Context HttpServletRequest request){
		
		RestSession session = (RestSession)request.getSession().getAttribute( RestSession.REST_SESSION_ATR );
		CurrentLocation currentLocation = session.getUser().getCurrentLocation();
		
		if(currentLocation == null){
			currentLocation = new CurrentLocation();
		}
		
		currentLocation.setDate(new Date());
		currentLocation.setLatitude(location.getLatitude());
		currentLocation.setLongitude(location.getLongitude());
		location.setDate(new Date());
		
		try{
			locationService.createLocationAndSaveCurrentLocation(location, currentLocation, session.getUser().getId());
		}catch(Exception e){
			e.printStackTrace();
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
		
		return Response.status( Response.Status.CREATED )
				.build();
	}
}
