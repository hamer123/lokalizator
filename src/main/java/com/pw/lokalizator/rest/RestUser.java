package com.pw.lokalizator.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.logging.Logger;

import com.pw.lokalizator.model.Role;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.model.UserSecurity;
import com.pw.lokalizator.repository.UserRepository;

@Path("/user")
//@Stateless
public class RestUser {
	//@PersistenceContext
	//EntityManager em;
	@EJB
	private UserRepository userRepository;
	
	Logger log = Logger.getLogger(RestUser.class);

	@GET
	//@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(value = {MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response findUSer(@PathParam("id") Long id){
		User user = null;
		user = userRepository.findById(id);
		if(user != null){
			return Response.status(200)
					       .entity(user)
					       .build();
		}else{
			return Response.status(404)
			               .build();
		}
	}
	
	@POST
	@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/create")
	public Response findUSer(User user){

		log.info("/user/create-> login " + user.getLogin() + ", password " + user.getPassword() + ", email " + user.getEmail());
		
		try{
			UserSecurity us = new UserSecurity();
			us.setRola(Role.USER);
			us.setServiceKey("1234");
			us.setTokenKey("4321");
			
			user.setUserSecurity(us);
			user = userRepository.add(user);
			return Response.status(200)
					       .entity(user)
					       .build();
		}catch(Exception e){
			log.error(e.toString());
			return Response.status(400)
					       .build();
		}
	}
	
	@DELETE
	@Path("/delete/{id}")
	public Response deleteUser(@PathParam("id") long id){
		try{
			userRepository.remove(id);
			return Response.status(200)
					       .build();
		}catch(Exception e){
			return Response.status(400)
			               .build();
		}
	}
	
}
