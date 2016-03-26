package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.repository.LocationRepository;

@Named(value="gmapView")
@ViewScoped
public class GMapView implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private LokalizatorSession session;
	@EJB
	private LocationRepository locationRepository;

	private MapModel emptyModel;
	//CENTER
    private double latCenter;
    private double lngCenter;
    //CURRENT MARKER
    private double latCurrent;
    private double lngCurrent;
    
    private String location;
    private Date fromDate;
    private Date endDate;
    private List<Location>locations;
    
	@PostConstruct
	private void postConstruct(){
		CurrentLocation currentLocation = session.getCurrentUser().getCurrentLocation();
		latCenter = currentLocation.getLatitude();
		lngCenter = currentLocation.getLongitude();
		latCurrent = currentLocation.getLatitude();
		lngCurrent = currentLocation.getLongitude();
		Marker marker = new Marker(new LatLng(latCurrent, lngCurrent), "current_pozycja");
		emptyModel = new DefaultMapModel();
		emptyModel.addOverlay(marker);
		
		location = createLocation(latCenter, lngCenter);
	}
	
	private String createLocation(double lat, double lng){
		return lat + ", " + lng;
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
		return locations;
	}

	public void setLocationList(List<Location> locationList) {
		this.locations = locationList;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void findLocations(ActionEvent actionEvent){
		try{
			//locations =  new ArrayList<Location>(locationRepository.getLocationYoungThanAndOlderThan(fromDate, endDate));
			locations = locationRepository.getLocationYoungThanAndOlderThan(fromDate, endDate);
		}catch(Exception e){
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not find locations",  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        //TODO
			e.printStackTrace();
		}
	}
	
	public void changeLocation(String latS, String lngS){
		double lat = Double.parseDouble(latS);
		double lng = Double.parseDouble(lngS);
		
	    latCenter = lat;
	    lngCenter = lng;
	    latCurrent = lat;
	    lngCurrent = lng;
	    
	    location = createLocation(lat, lng);
	    Marker marker = new Marker(new LatLng(lat, lng), "current_pozycja");
	    emptyModel.getMarkers().clear();
	    emptyModel.addOverlay(marker);
	    
	    System.err.println("ACTION LISTENER");
	}

}
