package com.pw.lokalizator.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.model.map.LatLng;

import com.pw.lokalizator.model.google.component.Route;
import com.pw.lokalizator.service.RouteService;

@Stateless
public class RouteServiceImpl implements RouteService{

	private static final double EARTH_DARIUS_KM = 6378.137;

	@Override
	public double calculateLenghtMeters(Route route) {
	    List<LatLng>points = route.getPolyline().getPaths();
	    double lenght = 0;
	    
	    for(int i = 0; i + 1 <points.size(); i++)
	    	lenght += calculateLenghtMeters(points.get(i), points.get(i + 1));
	    
		return lenght;
	}
	
	private double calculateLenghtMeters(LatLng point, LatLng point2){
	    double dLat = (point2.getLat() - point.getLat()) * Math.PI / 180;
	    double dLon = (point2.getLng() - point.getLng()) * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(point.getLat() * Math.PI / 180) * Math.cos(point2.getLat() * Math.PI / 180) *
	               Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = EARTH_DARIUS_KM * c;
	    return d * 1000;  // meters
	}

}
