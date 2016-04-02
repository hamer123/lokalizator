package com.pw.lokalizator.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class RestTest {
	
	@GET
	public String test(){
		return "TEST";
	}
}
