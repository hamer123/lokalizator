package com.pw.lokalizator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
import com.pw.lokalizator.model.Position;
import com.pw.lokalizator.model.Route;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.GoogleMapUserComponentService;

@Named
@ApplicationScoped
public class GoogleMapUserComponentServiceImpl implements GoogleMapUserComponentService{
	
	@Override
	public Route route(List<Location> locations) {
		Polyline polyline = PolylineBuilder.create(locations);
		
		int lastIndex = locations.size() - 1;
		Marker start =  MarkerBuilder.createMarker(locations.get(0));
		start.setIcon(MarkerBuilder.Icon.START_ROUTE_ICON_URL);
		Marker end = MarkerBuilder.createMarker(locations.get(lastIndex));
		end.setIcon(MarkerBuilder.Icon.END_ROUTE_ICON_URL);
		
		return new Route(start, end, polyline);
	}
	
	@Override
	public List<Position> lastLocations(User user) {
		List<Location>locations = getLocations(user);
		List<Position>positions = new ArrayList<Position>();
		
		for(Location location : locations)
			positions.add( new Position( MarkerBuilder.createMarker(location), CircleBuilder.createCircle(location) ) );

		return positions;
	}

	@Override
	public List<Position> lastLocations(Set<User> users) {
		List<Location>locations = new ArrayList<Location>();
		List<Position>positions = new ArrayList<Position>();
		
		for(User user : users)
			locations.addAll( getLocations(user) );
		
		for(Location location : locations)
			positions.add( new Position( MarkerBuilder.createMarker(location), CircleBuilder.createCircle(location) ) );
		
		return positions;
	}
	
	@Override
	public List<Position> lastLocations(User user,GoogleMapComponentVisible visible) {
		List<Position>positions = new ArrayList<Position>();
		
		if(user.getLastLocationGPS() != null)
			positions.add(positionGPS(user, visible));
		if(user.getLastLocationNetworkNaszaUsluga() != null)
			positions.add(positionNetworkNasz(user, visible));
		if(user.getLastLocationNetworObcaUsluga() != null)
			positions.add(positionNetworkObcy(user, visible));
		
		return positions;
	}

	@Override
	public List<Position> lastLocations(Set<User> users,GoogleMapComponentVisible visible) {
		List<Position>positions = new ArrayList<Position>();

		for(User user : users){
			if(user.getLastLocationGPS() != null)
				positions.add(positionGPS(user, visible));
			if(user.getLastLocationNetworkNaszaUsluga() != null)
				positions.add(positionNetworkNasz(user, visible));
			if(user.getLastLocationNetworObcaUsluga() != null)
				positions.add(positionNetworkObcy(user, visible));
		}
		
		return positions;
	}
	
	@Override
	public Position position(Location location) {
		Marker marker = MarkerBuilder.createMarker(location);
		Circle circle = CircleBuilder.createCircle(location);
		return new Position(marker, circle);
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
		return new Route(polyline);
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
	
	private Position positionGPS(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleGps())
			circle = CircleBuilder.createCircle(user.getLastLocationGPS());
		if(visible.isMarkerGps())
			marker = MarkerBuilder.createMarker(user.getLastLocationGPS());
		
		return new Position(marker, circle);
	}
	
	private Position positionNetworkNasz(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleNetworkNasz())
			circle = CircleBuilder.createCircle(user.getLastLocationNetworkNaszaUsluga());
		if(visible.isMarkerNetworkNasz())
			marker = MarkerBuilder.createMarker(user.getLastLocationNetworkNaszaUsluga());
		
		return new Position(marker, circle);
	}
	
	private Position positionNetworkObcy(User user, GoogleMapComponentVisible visible){
		Marker marker = null;
		Circle circle = null;
		
		if(visible.isCircleNetworkObcy())
			circle = CircleBuilder.createCircle(user.getLastLocationNetworObcaUsluga());
		if(visible.isMarkerNetworkObcy())
			marker = MarkerBuilder.createMarker(user.getLastLocationNetworObcaUsluga());
		
		return new Position(marker, circle);
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
