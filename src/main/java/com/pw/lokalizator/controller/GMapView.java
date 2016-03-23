package com.pw.lokalizator.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.repository.CurrentLocationRepository;


@Named(value="gmapView")
@ViewScoped
public class GMapView implements Serializable{
	@EJB
	private CurrentLocationRepository currentLocationRepository;
	
	private MapModel emptyModel;
    private double lat;
    private double lng;
    
	@PostConstruct
	private void postConstruct(){
		System.err.println("@PostConstruct-> GMapView");
		emptyModel = new DefaultMapModel();
		Marker marker = new Marker(new LatLng(51.601357, 18.972930), "pozycja");
		emptyModel.addOverlay(marker);
	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

}
