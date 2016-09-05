package com.pw.lokalizator.model.google.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pw.lokalizator.exception.NotSingleResultException;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.enums.Overlays;

public class GoogleMapModel implements MapModel, Serializable{

	private List<Marker> markers;
	
	private List<Polyline> polylines;
	
	private List<Polygon> polygons;
        
    private List<Circle> circles;
        
    private List<Rectangle> rectangles;

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
			markers.add((Marker) overlay);
		}
		else if(overlay instanceof Polyline) {
			polylines.add((Polyline) overlay);
		}
		else if(overlay instanceof Polygon) {
			polygons.add((Polygon) overlay);
		}
		else if(overlay instanceof Circle) {
			circles.add((Circle) overlay);
		}
		else if(overlay instanceof Rectangle) {
			rectangles.add((Rectangle) overlay);
		}
	}

	@SuppressWarnings("unchecked")
    @Deprecated
	public Overlay findOverlay(String id) {
		List list = getOverlayList(id);
		
		if(list != null){
			for(Iterator iterator = list.iterator(); iterator.hasNext();) {
				Overlay overlay = (Overlay) iterator.next();
				if(overlay.getId().equals(id))
					return overlay;
			}
		}
		return null;
	}
	
	public Overlay findSingleOverlay(OverlayIdentyfikator identyfikator){
		List<Overlay>list = findOverlay(identyfikator);
		if(list.size() > 1)
			throw new NotSingleResultException();
		else if(list.isEmpty())
			return null;
		else
			return list.get(0);
	}
	
	public void removeOverlay(OverlayIdentyfikator identyfikator){
		List<Overlay>list = getOverlayList(identyfikator.createIdentyfikator());
		
		if(list != null){
			removeOverlayFromList(list, identyfikator);
		} else {
			removeOverlayFromList(markers, identyfikator);
			removeOverlayFromList(polylines, identyfikator);
			removeOverlayFromList(polygons, identyfikator);
			removeOverlayFromList(circles, identyfikator);
			removeOverlayFromList(rectangles, identyfikator);
		}
	}
	
	public List<Overlay> findOverlay(OverlayIdentyfikator identyfikator){
		List<Overlay>list = getOverlayList(identyfikator.createIdentyfikator());
		List<Overlay>overlays = new ArrayList<Overlay>();
		
		if(list != null){
			overlays.addAll( findOverlayFromList(list, identyfikator) );
		} else {
			overlays.addAll( findOverlayFromList(markers, identyfikator) );
			overlays.addAll( findOverlayFromList(polylines, identyfikator) );
			overlays.addAll( findOverlayFromList(polygons, identyfikator) );
			overlays.addAll( findOverlayFromList(circles, identyfikator) );
			overlays.addAll( findOverlayFromList(rectangles, identyfikator) );
		}
		
		return overlays;
	}
	
	private <T extends Overlay> List<Overlay> findOverlayFromList(List<T>list, OverlayIdentyfikator identyfikator){
		List<Overlay>overlays = new ArrayList<Overlay>();
		Pattern pattern = identyfikator.createPattern();
		
		for(Overlay overlay : list){
			Matcher matcher = pattern.matcher(overlay.getId());
			
			if(matcher.matches())
				overlays.add(overlay);
		}
			
		return overlays;
	}
	
	private <T extends Overlay> void removeOverlayFromList(List<T>list, OverlayIdentyfikator identyfikator){
		Pattern pattern = identyfikator.createPattern();
		Iterator<T>it = list.iterator();
		
		while(it.hasNext()){
			T overlay = it.next();
			Matcher matcher = pattern.matcher(overlay.getId());
			
			if(matcher.matches()){
				it.remove();
			}
		}
	}
	
	private List<Overlay> getOverlayList(String id){
		List list = null;
		
		if(id.startsWith(Overlays.MARKER.toString()))
			list = markers;
		else if(id.startsWith(Overlays.POLYLINE.toString()))
			list = polylines;
		else if(id.startsWith(Overlays.POLYGON.toString()))
			list = polygons;
		else if(id.startsWith(Overlays.CIRCLE.toString()))
			list = circles;
		else if(id.startsWith(Overlays.RECTANGLE.toString()))
			list = rectangles;
		
		return list;
	}
	
	
	public static String center(LatLng latLng){
		return   latLng.getLat() 
			   + ", " 
			   + latLng.getLng();
	}
	
	public static String center(double lat, double lng){
		return   lat 
			   + ", " 
			   + lng;
	}
	
	public static String center(Location location){
		return   location.getLatitude()
			   + ", " 
			   + location.getLongitude();
	}
	
	public void clear(){
		markers.clear();
		polylines.clear();
		polygons.clear();
		circles.clear();
		rectangles.clear();
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
