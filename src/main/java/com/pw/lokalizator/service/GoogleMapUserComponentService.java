package com.pw.lokalizator.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.inject.Named;

import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.model.GoogleMapComponentVisible;
import com.pw.lokalizator.model.Position;
import com.pw.lokalizator.model.Route;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;

@Named
public interface GoogleMapUserComponentService {
	List<Position> lastLocations(User user);
	List<Position> lastLocations(Set<User> users);
	List<Position> lastLocations(User user,  GoogleMapComponentVisible visible);
	List<Position> lastLocations(Set<User> users,  GoogleMapComponentVisible visible);
	List<Polygon> polygons(User user);
	Polygon polygon(Area area);
	Polygon polygon(Area area, GoogleMapComponentVisible visible);
	Position position(Location location);
	Route route(List<Location>locations);
	Route routeNoMarkers(List<Location>locations);
}
