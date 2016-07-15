package com.pw.lokalizator.rest;

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

import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.service.LocationService;


@Path("/location/network")
public class RestLocationNetwork {
	@Inject
	private LocationService locationService;
	@Inject
	Logger logger;
	
	@POST()
	@Path("/create")
	@Consumes( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response createLocation(LocationNetwork locationNetwork, @Context HttpServletRequest request){
		try{
			RestSession session = (RestSession)request.getSession().getAttribute( RestSession.REST_SESSION_ATR );
			long id = session.getUser().getId();
			locationService.createLocationNetworkUpdateUserCurrentLocationNetwork(locationNetwork, id);
			
			return Response.status( Response.Status.CREATED )
					.build();
		}catch(Exception e){
			
			logger.error("[RestLocationNetwork] Nie ulado sie zapisac lokalizacji..." + e);
			return Response.status( Response.Status.EXPECTATION_FAILED )
					.build();
		}
	}
	
}
