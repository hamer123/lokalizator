package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.OverlayVisibility;
import com.pw.lokalizator.model.GoogleMapModel.GoogleMapModelBuilder;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.GoogleMaps;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.PolygonModelRepository;
import com.pw.lokalizator.repository.UserRepository;

@Named("googleMapControllerSingleMode")
@ViewScoped
public class GoogleMapControllerSingleMode implements Serializable{
	@EJB
	private UserRepository userRepository;
	@EJB
	private PolygonModelRepository polygonModelRepository;
	@EJB
	private LocationRepository locationRepository;
	
	private MapModel googleMapModel;
	private OverlayVisibility gpsVisibility;
	private OverlayVisibility networkObcaUslugaVisibilty;
	private OverlayVisibility networkWlasnaUslugaVisibility;
	
	private User user;
	private Date locationFromDate;
	private Date locationToDate;
	
	@PostConstruct
	private void init(){
		
	}
	
	public void setSingleUser(String login){
		user = userRepository.findUserWithPolygonsByLogin(login);
//		googleMapModel = createGoogleMapSingleUser();
	}
	
	public User getSingleUser(){
		return user;
	}
	
	private void updateSingleUserLocations(){
		List<Location>locations = locationRepository.findByUserIdWhereYoungerThanAndOlderThanOrderByDateDesc(
				                                        user.getId(),
				                                        locationFromDate,
				                                        locationToDate);
	}
	
	
	private List<Polyline>createPolylines(List<Location>locations){
		List<Polyline>polylines = new ArrayList<>();
		
		//TODO
		
		return null;
	}
	
	
//	private GoogleMapModel createGoogleMapSingleUser(){
		//TODO
//		List<Location>locations = null;//singleUser.getLocations();
//		
//		List<Circle>circles = createCircles(locations);
//		List<Marker>markers = createMarkers(locations);
//		List<Polyline>polylines = createPolylines(locations); //TODO
//		
//		return new GoogleMapModelBuilder()
//		              .circles(circles)
//		              .markers(markers)
//		              .polylines(polylines)
//		              .build();
//	}
}
