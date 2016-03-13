package com.pw.lokalizator.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.Dao;

@Stateless
@Path("/user")
public class UserRest {
	@EJB
	private Dao<User> userDao;
	
	@GET
	@Path("/create")
	public Response createUser(@QueryParam("login") String login, 
			                   @QueryParam("password") String password,
			                   @QueryParam("email") String email){
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		user.setEmail(email);
		try{
			userDao.persist(user);
		}catch(Exception e){
			return Response
					.ok("NIE DODANO")
					.build();
		}
		
		return Response
				.ok("DODANO")
				.build();
	}
	
	@GET
	@Path("/find/{id}")
	@Produces({"application/xml"})
	public User findUser(@PathParam("id") long id){
		User user = null;
		try{
			user = (User) userDao.findById(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	
	@GET
	@Path("/find/json/{id}")
	@Produces({"application/json"})
	public User findUserJSON(@PathParam("id") long id){
		User user = null;
		try{
			user = (User) userDao.findById(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	

}
