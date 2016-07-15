package com.pw.lokalizator.model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;

public class Position implements GoogleMapComponent{
	private Marker marker;
	private Circle circle;

	public Position(Marker marker, Circle circle) {
		this.marker = marker;
		this.circle = circle;
	}

	public Position(Marker marker) {
		this.marker = marker;
	}

	public Position(Circle circle) {
		this.circle = circle;
	}

	@Override
	public List<Overlay> overlays() {
		List<Overlay>overlays = new ArrayList<Overlay>();
		
		if(circle != null)
			overlays.add(circle);
		if(marker != null)
			overlays.add(marker);
		
		return overlays;
	}
	
	public Marker getMarker() {
		return marker;
	}

	public Circle getCircle() {
		return circle;
	}
}
