package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.NotFoundException;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

public class GoogleMapModel implements MapModel, Serializable{

	private List<Marker> markers;
	
	private List<Polyline> polylines;
	
	private List<Polygon> polygons;
        
    private List<Circle> circles;
        
    private List<Rectangle> rectangles;
	
	private final static String MARKER_ID_PREFIX = "marker";
	
	private final static String POLYLINE_ID_PREFIX = "polyline_";
	
	private final static String POLYGON_ID_PREFIX = "polygon_";
        
	private final static String CIRCLE_ID_PREFIX = "circle_";
	
    private final static String RECTANGLE_ID_PREFIX = "rectangle_";

	public GoogleMapModel(List<Marker> markers, List<Polyline> polylines,
			List<Polygon> polygons, List<Circle> circles,
			List<Rectangle> rectangles) {
		this.markers = markers;
		this.polylines = polylines;
		this.polygons = polygons;
		this.circles = circles;
		this.rectangles = rectangles;
	}

	public GoogleMapModel() {
		markers = new ArrayList<Marker>();
		polylines = new ArrayList<Polyline>();
		polygons = new ArrayList<Polygon>();
		circles = new ArrayList<Circle>();
		rectangles = new ArrayList<Rectangle>();
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public List<Polyline> getPolylines() {
		return polylines;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}
	
    public List<Circle> getCircles() {
		return circles;
	}
        
    public List<Rectangle> getRectangles() {
		return rectangles;
	}

	public void addOverlay(Overlay overlay) {
		if(overlay instanceof Marker) {
			overlay.setId(MARKER_ID_PREFIX + UUID.randomUUID().toString());
			markers.add((Marker) overlay);
		}
		else if(overlay instanceof Polyline) {
			overlay.setId(POLYLINE_ID_PREFIX + UUID.randomUUID().toString());
			polylines.add((Polyline) overlay);
		}
		else if(overlay instanceof Polygon) {
			overlay.setId(POLYGON_ID_PREFIX + UUID.randomUUID().toString());
			polygons.add((Polygon) overlay);
		}
		else if(overlay instanceof Circle) {
			overlay.setId(CIRCLE_ID_PREFIX + UUID.randomUUID().toString());
			circles.add((Circle) overlay);
		}
		else if(overlay instanceof Rectangle) {
			overlay.setId(RECTANGLE_ID_PREFIX + UUID.randomUUID().toString());
			rectangles.add((Rectangle) overlay);
		}
	}

	@SuppressWarnings("unchecked")
	public Overlay findOverlay(String id) {
		List list = null;
		
		if(id.startsWith(MARKER_ID_PREFIX))
			list = markers;
		else if(id.startsWith(POLYLINE_ID_PREFIX))
			list = polylines;
		else if(id.startsWith(POLYGON_ID_PREFIX))
			list = polygons;
		else if(id.startsWith(CIRCLE_ID_PREFIX))
			list = circles;
		else if(id.startsWith(RECTANGLE_ID_PREFIX))
			list = rectangles;
		
		for(Iterator iterator = list.iterator(); iterator.hasNext();) {
			Overlay overlay = (Overlay) iterator.next();
			
			if(overlay.getId().equals(id))
				return overlay;
		}
		
		return null;
	}
	
	public static class GoogleMapModelBuilder{
		private List<Circle>circleList = new ArrayList<Circle>();
		private List<Marker>markerList = new ArrayList<Marker>();
		private List<Polygon>polygonList = new ArrayList<Polygon>();
		private List<Polyline>polylineList = new ArrayList<Polyline>();
		private List<Rectangle>rectangleList = new ArrayList<Rectangle>();
		
		public GoogleMapModelBuilder(){}
		
		public GoogleMapModelBuilder circles(List<Circle>circles){
			this.circleList = circles;
			return this;
		}
		
		public GoogleMapModelBuilder markers(List<Marker>markers){
			this.markerList = markers;
			return this;
		}
		
		public GoogleMapModelBuilder polygon(List<Polygon>polygons){
			this.polygonList = polygons;
			return this;
		}
		
		public GoogleMapModelBuilder polylines(List<Polyline>polylines){
			this.polylineList = polylines;
			return this;
		}
		
		public GoogleMapModelBuilder rectangles(List<Rectangle>rectangles){
			this.rectangleList = rectangles;
			return this;
		}
		
		public GoogleMapModel build(){
			return new GoogleMapModel(
					markerList,
					polylineList,
					polygonList,
					circleList,
					rectangleList
					);
		}
	}
}
