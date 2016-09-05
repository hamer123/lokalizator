package com.pw.lokalizator.rest.resource.location;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.lokalizator.security.restful.Secured;
import org.jboss.logging.Logger;
import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.service.LocationService;

@Secured
@Path("/location/gps")
public class RestLocationGPS {
	@Inject
	private LocationService locationService;
	@Inject
	Logger logger;
	
	@POST()
	@Path("/create")
	@Consumes( value = {MediaType.APPLICATION_JSON} )
	@Produces( value = {MediaType.APPLICATION_JSON} )
	public Response createLocation(LocationGPS locationGPS , @Context RestSession restSession){
		try{
			long id = restSession.getUser().getId();
			locationService.createLocationGPSUpdateUserCurrentLocationGPS(locationGPS, id);
			return Response.status(Response.Status.CREATED).build();
		}catch(Exception e){
			logger.error(" Nie ulado sie zapisac lokalizacji..." + e);
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
	}
	
}
