package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	private static Logger LOG = Logger.getLogger(GoogleMapModel.class);
	private List<Circle>circleList = new ArrayList<Circle>();
	private List<Marker>markerList = new ArrayList<Marker>();
	private List<Polygon>polygonList = new ArrayList<Polygon>();
	private List<Polyline>polylineList = new ArrayList<Polyline>();
	private List<Rectangle>rectangleList = new ArrayList<Rectangle>();
		
	public GoogleMapModel() {}
	
	public GoogleMapModel(List<Circle> circleList, List<Marker> markerList,
			List<Polygon> polygonList, List<Polyline> polylineList,
			List<Rectangle> rectangleList) {
		this.circleList = circleList;
		this.markerList = markerList;
		this.polygonList = polygonList;
		this.polylineList = polylineList;
		this.rectangleList = rectangleList;
	}

	@Override
	public void addOverlay(Overlay overlay) {
		Class overlayType = overlay.getClass();
		
		 if     (overlayType == Circle.class)
			circleList.add((Circle) overlay);
		 else if(overlayType == Marker.class) 
			markerList.add((Marker)overlay);
		 else if(overlayType == Polygon.class) 
			polygonList.add((Polygon)overlay);
		 else if(overlayType == Polyline.class) 
			polylineList.add((Polyline)overlay);
		 else if(overlayType == Rectangle.class) 
			rectangleList.add((Rectangle)overlay);
		 else 
			throw new IllegalArgumentException("[GoogleMapModel] Objekt nie implementuje interfejsu org.primefaces.model.map.Overlay");

	}

	@Override
	public Overlay findOverlay(String id) {
		Overlays overlayType = getOverlayTypeFromId(id);
		
		switch(overlayType){
		case CIRCLE:
			for(Overlay overlay : circleList){
				if(overlay.getId().equals(id))
					return overlay;
			}
			break;
			
		case MARKER:
			for(Overlay overlay : markerList){
				if(overlay.getId().equals(id))
					return overlay;
			}
			break;
			
		case POLYGON:
			for(Overlay overlay : polygonList){
				if(overlay.getId().equals(id))
					return overlay;
			}
			break;
			
		case POLYLINE:
			for(Overlay overlay : polylineList){
				if(overlay.getId().equals(id))
					return overlay;
			}
			break;
		case RECTANGLE:
			for(Overlay overlay : markerList){
				if(overlay.getId().equals(id))
					return overlay;
			}
			break;
		default:
			throw new IllegalArgumentException("[GoogleMapModel] Nie obslugiwany OverlayType: " + overlayType);
		}
		
		throw new NotFoundException();
	}
	
	private Overlays getOverlayTypeFromId(String id){
		String overlayType;
		overlayType = id.substring(0, id.indexOf("_"));
		return Overlays.valueOf(overlayType);
	}
	
	@Override
	public List<Circle> getCircles() {
		return circleList;
	}

	@Override
	public List<Marker> getMarkers() {
		return markerList;
	}

	@Override
	public List<Polygon> getPolygons() {
		return polygonList;
	}

	@Override
	public List<Polyline> getPolylines() {
		return polylineList;
	}

	@Override
	public List<Rectangle> getRectangles() {
		return rectangleList;
	}
	
	public void clear(){
		circleList = new ArrayList<Circle>();
		markerList = new ArrayList<Marker>();
		polygonList = new ArrayList<Polygon>();
		polylineList = new ArrayList<Polyline>();
		rectangleList = new ArrayList<Rectangle>();
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
					circleList,
					markerList,
					polygonList,
					polylineList,
					rectangleList
					);
		}
	}
}
