package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.model.google.component.Route;
import com.pw.lokalizator.model.entity.Location;

public class RouteManager {
	private Map<String,Route>routes = new HashMap<>(); 

	/**
	 * Dodaje nowa instacje road do mapy, jesli nie ma lokacji startowej tworzy empty route
	 * inaczej tworzy routa z sciezka bez markerow
	 * */
	public void add(String login, Location location){
		if(location != null) {
			Polyline polyline = PolylineBuilder.createNoData(location);
			Route route = Route.onlyPolyline(polyline);
			routes.put(login, route);
		} else {
			throw new IllegalArgumentException("Location can not be null");
		}
	}

	/** Dodaj lokacje do sciezki , jesli nie bylo wczesniej tworzy nowa */
	public void update(String login, Location location){
		Route route = routes.get(login);
		if(route == null){
			add(login, location);
		} else {
			route.addPath(location);
		}
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
