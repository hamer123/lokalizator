package com.pw.lokalizator.job;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

import com.pw.lokalizator.model.entity.Location;

@Stateless
public class LocationEventHandler {
	
	public void handleEvent(@Observes Location location){
		System.out.println("HERE WILL BE HANDLE LOCATION EVENT " + location.getId());
	}
}
