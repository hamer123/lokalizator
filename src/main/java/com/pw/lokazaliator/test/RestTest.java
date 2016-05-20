package com.pw.lokazaliator.test;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.WifiInfo;

@Path(value = "/test")
public class RestTest {

	@Path(value = "/wifiInfo")
	@POST
	public Response createWifiInfo(WifiInfo wifiInfo){
		
		System.out.println(wifiInfo);
		
		
		return Response.ok()
				       .build();
	}
	
	@Path(value = "/net")
	@POST
	public void testNetLoc(LocationNetwork network){
		
		System.out.println(network.getInfoWifi());
		System.out.println(network);
	}
}
