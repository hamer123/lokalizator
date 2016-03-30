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

import org.apache.commons.logging.Log;
import org.jboss.resteasy.logging.Logger;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.repository.CurrentLocationRepository;
import com.pw.lokalizator.repository.LocationRepository;

@Named(value="location")
@ViewScoped
public class LocationView implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private LokalizatorSession session;
	@Inject
	private GoogleMapSetting setting;
	@EJB
	private LocationRepository locationRepository;
	@EJB
	private CurrentLocationRepository currentLocationRepository;
	
	Logger log = Logger.getLogger(LocationView.class);
	
	private MapModel map;
    private Date fromDate;
    private Date endDate;
    private Date maxDate;
    private Marker oldMarker;
    private Marker currentMarker;
    
    private List<Location>locations;
    
	@PostConstruct
	private void postConstruct(){
		CurrentLocation currentLocation = session.getCurrentUser().getCurrentLocation();
		setting.setCenter(new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ));
		currentMarker = new Marker( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ), "Twoja pozycja " + currentLocation.getDate() );
		map = new DefaultMapModel();
		map.addOverlay(currentMarker);
		maxDate = new Date();
	}
	
	public void findLocations(ActionEvent actionEvent){
		try{
			locations = locationRepository.getLocationYoungThanAndOlderThan(fromDate, endDate);
			
		}catch(Exception e){
			log.info(e.toString());
			
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not find locations",  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void changeLocation(String index){
		Location choosed = locations.get( Integer.valueOf(index) );
		
		if(oldMarker == null){
			oldMarker = new Marker(new LatLng( choosed.getLatitude(), choosed.getLongitude() ), "Your location at " + choosed.getDate() );
			setting.setCenter(new LatLng( choosed.getLatitude(), choosed.getLongitude() ));
			map.addOverlay(oldMarker);
		}else{
			oldMarker.setLatlng(new LatLng( choosed.getLatitude(), choosed.getLongitude() ));
			setting.setCenter(new LatLng( choosed.getLatitude(), choosed.getLongitude() ));
		}
	    
	}
	
	public void onStateChanged(StateChangeEvent event){
		setting.setCenter(event.getCenter());
		setting.setZoom(event.getZoomLevel());
	}
	
	public void onPolling(){
		System.err.println("POLLING");
		try{
			CurrentLocation location = currentLocationRepository.findByUserId( session.getCurrentUser().getId() );
			currentMarker.setLatlng( new LatLng( location.getLatitude(), location.getLongitude() ));
			session.getCurrentUser().setCurrentLocation(location);
			System.err.println("POLLING !!! " + location.getLatitude() + ", " + location.getLongitude());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////// GET AND SET ///////////////////////////////////////////////////////////////////

	public GoogleMapSetting getSetting() {
		return setting;
	}

	public void setSetting(GoogleMapSetting setting) {
		this.setting = setting;
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
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

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
}
