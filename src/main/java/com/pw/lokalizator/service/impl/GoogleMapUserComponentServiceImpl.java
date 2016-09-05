package com.pw.lokalizator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.pw.lokalizator.service.GoogleMapUserComponentService;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.jsf.utilitis.CircleBuilder;
import com.pw.lokalizator.jsf.utilitis.MarkerBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.jsf.utilitis.PolylineBuilder;
import com.pw.lokalizator.model.google.map.GoogleMapComponentVisible;
import com.pw.lokalizator.model.google.component.GoogleLocation;
import com.pw.lokalizator.model.google.component.Route;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;

@Named
@ApplicationScoped
public class GoogleMapUserComponentServiceImpl implements GoogleMapUserComponentService {
	
	@Override
	public Route route(List<Location> locations) {
		Polyline polyline = PolylineBuilder.create(locations);
		
		int lastIndex = locations.size() - 1;
		Marker start =  MarkerBuilder.createMarker(locations.get(0));
		start.setIcon(MarkerBuilder.Icon.START_ROUTE_ICON_URL);
		Marker end = MarkerBuilder.createMarker(locations.get(lastIndex));
		end.setIcon(MarkerBuilder.Icon.END_ROUTE_ICON_URL);
		
		return Route.full(start, end, polyline);
	}
	
	@Override
	public List<GoogleLocation> lastLocations(User user) {
		List<Location>locations = getLocations(user);
		List<GoogleLocation> googleLocations = new ArrayList<GoogleLocation>();
		
		for(Location location : locations)
			googleLocations.add( new GoogleLocation( MarkerBuilder.createMarker(location), CircleBuilder.createCircle(location) ) );

		return googleLocations;
	}

	@Override
	public List<GoogleLocation> lastLocations(Set<User> users) {
		List<Location>locations = new ArrayList<Location>();
		List<GoogleLocation> googleLocations = new ArrayList<GoogleLocation>();
		
		for(User user : users)
			locations.addAll( getLocations(user) );
		
		for(Location location : locations)
			googleLocations.add( new GoogleLocation( MarkerBuilder.createMarker(location), CircleBuilder.createCircle(location) ) );
		
		return googleLocations;
	}
	
	@Override
	public List<GoogleLocation> lastLocations(User user, GoogleMapComponentVisible visible) {
		List<GoogleLocation> googleLocations = new ArrayList<GoogleLocation>();
		
		if(user.getLastLocationGPS() != null)
			googleLocations.add(positionGPS(user, visible));
		if(user.getLastLocationNetworkNaszaUsluga() != null)
			googleLocations.add(positionNetworkNasz(user, visible));
		if(user.getLastLocationNetworObcaUsluga() != null)
			googleLocations.add(positionNetworkObcy(user, visible));
		
		return googleLocations;
	}

	@Override
	public List<GoogleLocation> lastLocations(Set<User> users, GoogleMapComponentVisible visible) {
		List<GoogleLocation> googleLocations = new ArrayList<GoogleLocation>();

		for(User user : users){
			if(user.getLastLocationGPS() != null)
				googleLocations.add(positionGPS(user, visible));
			if(user.getLastLocationNetworkNaszaUsluga() != null)
				googleLocations.add(positionNetworkNasz(user, visible));
			if(user.getLastLocationNetworObcaUsluga() != null)
				googleLocations.add(positionNetworkObcy(user, visible));
		}
		
		return googleLocations;
	}
	
	@Override
	public GoogleLocation position(Location location) {
		Marker marker = MarkerBuilder.createMarker(location);
		Circle circle = CircleBuilder.createCircle(location);
		return new GoogleLocation(marker, circle);
	}
	

	@Override
	public List<Polygon> polygons(User user) {
		List<Polygon>polygons = new ArrayList<Polygon>();
		
		for(Area area : user.getAreas())
			polygons.add( PolygonBuilder.create(area) );
		
		return polygons;
	}

	@Override
	public Polygon polygon(Area area) {
		return PolygonBuilder.create(area);
	}
	

	@Override
	public Polygon polygon(Area area, GoogleMapComponentVisible visible) {
		if(shoudlCreatePolygon(area, visible))
			return PolygonBuilder.create(area);
		
		return null;
	}
	
	@Override
	public Route routeNoMarkers(List<Location> locations) {
		Polyline polyline = PolylineBuilder.create(locations);
		return Route.onlyPolyline(polyline);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////   PRIVATES    //////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean shoudlCreatePolygon(Area area, GoogleMapComponentVisible visible){
		if(area.isAktywny() && visible.isActivePolygon()) 
			return true;
		else if(!area.isAktywny() && visible.isNotActivePolygon())
			return true;
		else
			return false;
	}
	
	private GoogleLocation positionGPS(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleGps())
			circle = CircleBuilder.createCircle(user.getLastLocationGPS());
		if(visible.isMarkerGps())
			marker = MarkerBuilder.createMarker(user.getLastLocationGPS());
		
		return new GoogleLocation(marker, circle);
	}
	
	private GoogleLocation positionNetworkNasz(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleNetworkNasz())
			circle = CircleBuilder.createCircle(user.getLastLocationNetworkNaszaUsluga());
		if(visible.isMarkerNetworkNasz())
			marker = MarkerBuilder.createMarker(user.getLastLocationNetworkNaszaUsluga());
		
		return new GoogleLocation(marker, circle);
	}
	
	private GoogleLocation positionNetworkObcy(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleNetworkObcy())
			circle = CircleBuilder.createCircle(user.getLastLocationNetworObcaUsluga());
		if(visible.isMarkerNetworkObcy())
			marker = MarkerBuilder.createMarker(user.getLastLocationNetworObcaUsluga());
		
		return new GoogleLocation(marker, circle);
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
