package com.pw.lokalizator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.jsf.utilitis.CircleBuilder;
import com.pw.lokalizator.jsf.utilitis.MarkerBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.jsf.utilitis.PolylineBuilder;
import com.pw.lokalizator.model.GoogleMapComponentVisible;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.GoogleMapUserComponentService;

@Stateless
public class GoogleMapUserComponentServiceImpl implements GoogleMapUserComponentService{
	
	@Override
	public List<Overlay> route(List<Location> locations) {
		List<Overlay>result = new ArrayList<Overlay>();
		
		result.add( PolylineBuilder.create(locations) );
		
		int lastIndex = locations.size() - 1;
		result.add( MarkerBuilder.createMarker(locations.get(0)) );
		result.add( MarkerBuilder.createMarker(locations.get(lastIndex)) );
		
		return result;
	}
	
	@Override
	public List<Overlay> lastLocation(User user) {
		List<Location>locations = getLocations(user);
		List<Overlay>overlays = new ArrayList<Overlay>();
		
		for(Location location : locations){
			overlays.add( MarkerBuilder.createMarker(location) );
			overlays.add( CircleBuilder.createCircle(location) );
		}
		
		for(Area area : user.getArea()){
			overlays.add( PolygonBuilder.create(area) );
		}
		
		return overlays;
	}

	@Override
	public List<Overlay> lastLocation(Set<User> users) {
		List<Location>locations = new ArrayList<Location>();
		List<Overlay>overlays = new ArrayList<Overlay>();
		
		for(User user : users){
			locations.addAll( getLocations(user) );
		}
		
		for(Location location : locations){
			overlays.add( MarkerBuilder.createMarker(location) );
			overlays.add( CircleBuilder.createCircle(location) );
		}
		
		for(User user : users){
			for(Area area : user.getArea()){
				overlays.add( PolygonBuilder.create(area) );
			}
		}
		
		return overlays;
	}
	
	@Override
	public List<Overlay> lastLocation(User user,
			GoogleMapComponentVisible visible) {
		
		List<Overlay>overlays = new ArrayList<Overlay>();
		overlays.addAll( circle(user, visible) );
		overlays.addAll( marker(user, visible) );
		overlays.addAll( polygon(user, visible) );
		
		return overlays;
	}

	@Override
	public List<Overlay> lastLocation(Set<User> users,
			GoogleMapComponentVisible visible) {

		List<Overlay>overlays = new ArrayList<Overlay>();
		for(User user : users){
			overlays.addAll( circle(user, visible) );
			overlays.addAll( marker(user, visible) );
			overlays.addAll( polygon(user, visible) );
		}
		
		return overlays;
	}
	
	private List<Circle> circle(User user, GoogleMapComponentVisible visible){
		List<Circle>circles = new ArrayList<Circle>();
		
		if(visible.isCircleGps() && user.getLastLocationGPS() != null)
			circles.add( CircleBuilder.createCircle(user.getLastLocationGPS()) );
		
		if(visible.isCircleNetworkNasz() && user.getLastLocationNetworkNaszaUsluga() != null)
			circles.add( CircleBuilder.createCircle(user.getLastLocationNetworkNaszaUsluga()) );
		
		if(visible.isCircleNetworkObcy() && user.getLastLocationNetworObcaUsluga() != null)
			circles.add( CircleBuilder.createCircle(user.getLastLocationNetworObcaUsluga()) );
		
		return circles;
	}
	
	private List<Marker> marker(User user, GoogleMapComponentVisible visible){
		List<Marker>markers = new ArrayList<Marker>();
		
		if(visible.isMarkerGps() && user.getLastLocationGPS() != null)
			markers.add( MarkerBuilder.createMarker(user.getLastLocationGPS()) );
		
		if(visible.isMarkerNetworkNasz() && user.getLastLocationNetworkNaszaUsluga() != null)
			markers.add( MarkerBuilder.createMarker(user.getLastLocationNetworkNaszaUsluga()) );
		
		if(visible.isMarkerNetworkObcy() && user.getLastLocationNetworObcaUsluga() != null)
			markers.add( MarkerBuilder.createMarker(user.getLastLocationNetworObcaUsluga()) );
		
		return markers;
	}
	
	private List<Polygon> polygon(User user, GoogleMapComponentVisible visible){
		List<Polygon>polygons = new ArrayList<Polygon>();
		
		if(visible.isPolygon()){
			for(Area area : user.getArea())
				polygons.add( PolygonBuilder.create(area) ); 
		}
		
		return polygons;
	}
	
	
	
	private List<Location> getLocations(User user){
		List<Location>locations = new ArrayList<Location>();
		
		Location location = user.getLastLocationGPS();
		addToListIfNotNull(locations, location);
		
		location = user.getLastLocationNetworkNaszaUsluga();
		addToListIfNotNull(locations, location);
		
		location = user.getLastLocationNetworObcaUsluga();
		addToListIfNotNull(locations, location);
		
		return locations;
	}
	
	private void addToListIfNotNull(List<Location>locations, Location location){
		if(location != null)
			locations.add(location);
	}

}
