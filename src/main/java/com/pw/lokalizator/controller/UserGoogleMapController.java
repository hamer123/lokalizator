package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.enums.GoogleMaps;

@Named
@Dependent
@Default
public class GoogleMapController implements Serializable{
	private GoogleMapModel googleMapModel;
	private int zoom;
	private String center;
	private boolean streetVisible;
	private GoogleMaps googleMapType;
	
	@PostConstruct
	public void postConstruct(){
		googleMapModel = new GoogleMapModel();
		zoom = 15;
		center = "51.6014053, 18.9724216";
		streetVisible = true;
		googleMapType = GoogleMaps.HYBRID;
	}
	
	public void addOverlay(List<Overlay>overlays){
		for(Overlay overlay : overlays)
			googleMapModel.addOverlay(overlay);
	}
	
	public List<Overlay>findOverlay(OverlayIdentyfikator identyfikator){
		return googleMapModel.findOverlay(identyfikator);
	}
	
	public void removeOverlay(OverlayIdentyfikator identyfikator){
		googleMapModel.removeOverlay(identyfikator);
	}
	
	public void addOverlay(Overlay overlay){
		googleMapModel.addOverlay(overlay);
	}
	
	public void replace(List<Overlay>overlays){
		googleMapModel.clear();
		addOverlay(overlays);
	}
	
	public void clear(){
		googleMapModel.clear();
	}
	
	public void onGoogleMapStateChange(StateChangeEvent event){
		center = GoogleMapModel.center(event.getCenter());
        zoom = event.getZoomLevel();
	}
	
	public void onOverlaySelect(OverlaySelectEvent event){
		Object overlay = event.getOverlay();
		
		if(overlay instanceof Marker){
			Location location = (Location) event.getOverlay().getData();
			JsfMessageBuilder.infoMessage( messageMarker(location) );
		} else if(overlay instanceof Circle){
			Location location = (Location) event.getOverlay().getData();
			JsfMessageBuilder.infoMessage( messageCircle(location) );
		} else if(overlay instanceof Polygon){
			Area area = (Area) event.getOverlay().getData();
			JsfMessageBuilder.infoMessage( messagePolygon(area) );	
		}

	}
	
	public GoogleMapModel getGoogleMapModel() {
		return googleMapModel;
	}
	
	public void setGoogleMapModel(GoogleMapModel googleMapModel) {
		this.googleMapModel = googleMapModel;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public String getCenter() {
		return center;
	}
	
	public void setCenter(String center) {
		this.center = center;
	}
	
	public boolean isStreetVisible() {
		return streetVisible;
	}
	
	public void setStreetVisible(boolean streetVisible) {
		this.streetVisible = streetVisible;
	}
	
	public GoogleMaps getGoogleMapType() {
		return googleMapType;
	}
	
	public void setGoogleMapType(GoogleMaps googleMapType) {
		this.googleMapType = googleMapType;
	}
	
	private String messagePolygon(Area area){
		return   "Obszar sledzenia: "
			   + area.getName()
			   + " Target: "
			   + area.getTarget().getLogin()
			   + " Typ: "
			   + area.getAreaFollowType()
			   + " Aktywny: "
			   + area.isAktywny();
	}
	
	private String messageCircle(Location location){
		return  messageMarker(location) + 
				" Dokładność: " + 
				location.getAccuracy();
	}

	private String messageMarker(Location location){
		return  location.getProviderType() +
				" " + 
	            location.getUser().getLogin() +
	            " " +  
				location.getDate();
	}
}
