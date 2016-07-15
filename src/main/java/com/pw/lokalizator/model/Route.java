package com.pw.lokalizator.model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polyline;

public class Route implements GoogleMapComponent{

	private Marker start;
	private Marker end;
	private Polyline polyline;
	
	@Override
	public List<Overlay> overlays() {
		List<Overlay>overlays = new ArrayList<Overlay>();
		overlays.add(start);
		overlays.add(end);
		overlays.add(polyline);
		return overlays;
	}
	
	
	public Route(Marker start, Marker end, Polyline polyline){
		this.start = start;
		this.end = end;
		this.polyline = polyline;
	}

	public Marker getStart() {
		return start;
	}

	public Marker getEnd() {
		return end;
	}

	public Polyline getPolyline() {
		return polyline;
	}
}
