package com.pw.lokalizator.rest.resource.location;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pw.lokalizator.security.restful.Secured;
import org.jboss.logging.Logger;

import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.repository.LocationNetworkRepository;
import com.pw.lokalizator.service.LocationService;
import com.pw.lokalizator.service.inceptor.LoggingInterceptor;

@Secured
@Path(RestLocationNetwork.MAIN_PATH)
@Interceptors(value = LoggingInterceptor.class)
public class RestLocationNetwork {
	public static final String MAIN_PATH = "/location/network";
	public static final String CREATE_PATH = "/create";
	public static final String FIND_PATH = "/find/{id}";
	@Inject
	private LocationService locationService;
	@Inject
	private LocationNetworkRepository LocationNetworkRepository;
	@Inject
	private Logger logger;
	
	@POST
	@Path(CREATE_PATH)
	@Consumes( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response createLocation(LocationNetwork locationNetwork, @Context RestSession restSession){
		long id = restSession.getUser().getId();
		locationService.createLocationNetworkUpdateUserCurrentLocationNetwork(locationNetwork, id);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path(FIND_PATH)
	@Produces( value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	public Response findLocation(@PathParam("id") long id){
		try{
			Location location = LocationNetworkRepository.findById(id);
			return Response.status( Response.Status.OK )
					       .entity(location)
					       .build();
		}catch(Exception e){
			return Response.status( Response.Status.NOT_FOUND )
					       .build();
		}
	}

}
