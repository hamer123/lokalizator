package com.pw.lokalizator.controller;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

import org.primefaces.model.map.LatLng;

@Named
@Dependent
public class GoogleMapSetting implements Serializable{
	private GoogleMapType googleMapType;
	private boolean streetView;
	private String center;
	private int zoom;
	
	public GoogleMapSetting() {
		googleMapType = GoogleMapType.HYBRID;
		streetView = false;
		center = "51.601357, 18.97293";
		zoom = 12;
	}

	public GoogleMapType getGoogleMapType() {
		return googleMapType;
	}

	public void setGoogleMapType(GoogleMapType googleMapType) {
		this.googleMapType = googleMapType;
	}

	public boolean isStreetView() {
		return streetView;
	}

	public void setStreetView(boolean streetView) {
		this.streetView = streetView;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public void setCenter(LatLng latLng){
		center = latLng.getLat() + ", " + latLng.getLng(); 
	}
}
