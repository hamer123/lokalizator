package com.pw.lokalizator.model.enums;

import javax.ws.rs.NotFoundException;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

public enum Overlays {
   CIRCLE,MARKER,POLYGON,POLYLINE,RECTANGLE;

   public static Overlays convert(Class<? extends Overlay> type){
	   
	if(type == Circle.class)
		return CIRCLE;
	
	if(type == Marker.class)
		return MARKER;
	
	if(type == Polygon.class)
		return POLYGON;
	
	if(type == Polyline.class)
		return POLYLINE;
	
	if(type == Rectangle.class)
		return RECTANGLE;
	
	throw new NotFoundException("Ta klasa nie ma konwercji do enum " + type);
   }
}
