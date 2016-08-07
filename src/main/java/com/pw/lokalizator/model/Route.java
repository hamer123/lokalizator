package com.pw.lokalizator.model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.model.entity.Location;

public class Route implements GoogleMapComponent{

	private Marker start;
	private Marker end;
	private Polyline polyline;
	private boolean visible = true;
	
	@Override
	public List<Overlay> overlays() {
		List<Overlay>overlays = new ArrayList<Overlay>();
		if(start != null) overlays.add(start);
		if(end != null) overlays.add(end);
		overlays.add(polyline);
		return overlays;
	}

	public Route(Marker start, Marker end, Polyline polyline){
		this.start = start;
		this.end = end;
		this.polyline = polyline;
	}

	public boolean addPath(Location location){
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		getLocations().add(location);
		return polyline.getPaths().add(latLng);
	}
	
	@SuppressWarnings("unchecked")
	public List<Location>getLocations(){
		return (List<Location>) polyline.getData();
	}
	
	public Polyline getPolyline(){
		return polyline;
	}
	
	public Location getLastLocation(){
		return getLocations().get(getLocations().size() - 1);
	}
	
	public Route(Polyline polyline){
		this.polyline = polyline;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setColor(String color){
		polyline.setStrokeColor(color);
	}
}
