package com.pw.lokalizator.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(value = "/test")
public class TestRestEasy {

	@GET
	public Response testGET(){
		return Response.ok("GET-> TEST RestEASY")
		               .build();
	}
	
	@POST
	public Response testPOST(){
		return Response.ok("POST-> TEST RestEASY")
	               .build();
	}
}
