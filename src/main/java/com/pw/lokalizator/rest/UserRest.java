package com.pw.lokalizator.rest;

import javax.ejb.EJB;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.pw.lokalizator.repository.UserDao;

@Path("/user")
public class UserRest {

	@EJB
	private UserDao userDao;
	
	@GET
	@Path("/create")
	public Response createUser(@QueryParam("login") String login, 
			                   @QueryParam("password") String password,
			                   @QueryParam("email") String email){
		
		try{
			
		}catch(Exception e){
			
		}
		
		return Response
				.ok()
				.build();
	}
}
