package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.model.Route;
import com.pw.lokalizator.model.entity.Location;

public class RouteManager<T extends Location> {
	private Map<String,Route>routes = new HashMap<>(); 
	
	public void add(String login, Location location){
		List<Location>locations = new ArrayList<>();
		locations.add(location);
		Polyline polyline = PolylineBuilder.create(locations);
		Route route = new Route(polyline);
		routes.put(login, route);
	}
	
	public void update(String login, Location location){
		Route route = routes.get(login);
		route.addPath(location);
	}
	
	public void clear(){
		routes.clear();
	}
	
	public Route remove(String login){
		return routes.remove(login);
	}
	
	public List<Route>getAll(){
		return new ArrayList<>(routes.values());
	}
	
	public Route find(String login){
		return routes.get(login);
	}
	
	public List<Route>getVisible(){
		return routes.values()
		             .stream()
		             .filter( r -> r.isVisible())
		             .collect(Collectors.toList());         
	}
	
	public void setVisible(String login, boolean visible){
		Route route = routes.get(login);
		route.setVisible(visible);
	}
	
	public void setColor(String login, String color){
		Route route = routes.get(login);
		route.setColor(color);
	}
	
}
