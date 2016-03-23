package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.repository.CurrentLocationRepository;


@Named(value="gmapView")
@ViewScoped
public class GMapView implements Serializable{
	@EJB
	private CurrentLocationRepository currentLocationRepository;
	@Inject
	private LokalizatorSession session;
	
	private MapModel emptyModel;
    private double lat;
    private double lng;
    private Date fromDate;
    private Date endDate;
    private List<Location>locationList;
    
	@PostConstruct
	private void postConstruct(){
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

}
