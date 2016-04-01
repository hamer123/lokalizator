package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.exception.FriendArleadyExist;
import com.pw.lokalizator.model.CurrentLocation;
import com.pw.lokalizator.model.Friend;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.CurrentLocationRepository;
import com.pw.lokalizator.repository.FriendRepository;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.service.FriendService;

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
	@EJB
	private FriendRepository friendRepository;
	@EJB
	private FriendService friendService;
	
	Logger log = Logger.getLogger(LocationView.class);
	
	private MapModel map;
    private Date fromDate;
    private Date endDate;
    private Date maxDate;
    private Marker oldMarker;
    private Marker currentMarker;
    private String friendNick;
    private Location selectedLocation;
    private List<Location>locations;
    private List<Friend>friends;
    private Map<Long, Marker>friendsMarker;
    
	@PostConstruct
	private void postConstruct(){
		CurrentLocation currentLocation = session.getCurrentUser().getCurrentLocation();
		setting.setCenter(new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ));
		currentMarker = new Marker( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ), "Twoja pozycja [ " + currentLocation.getDate() + " ]");
		map = new DefaultMapModel();
		map.addOverlay(currentMarker);
		maxDate = new Date();
		friendsMarker = new HashMap<Long, Marker>();
		friends = friendRepository.findByUserId( session.getCurrentUser().getId() );
		for(Friend f : friends){
			Marker m = new Marker( new LatLng( f.getFriend().getCurrentLocation().getLatitude(), f.getFriend().getCurrentLocation().getLongitude()),
					               f.getFriend().getLogin() + " [ " + f.getFriend().getCurrentLocation().getDate() + " ]");
			friendsMarker.put(f.getFriend().getId() , m);
			map.addOverlay(m);
		}
	}
	
	/*
	 * Find your location in pass
	 */
	public void findLocations(ActionEvent actionEvent){
		try{
			locations = locationRepository.getLocationYoungThanAndOlderThan(fromDate, endDate);
			
		}catch(Exception e){
			e.printStackTrace();
			
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not find locations",  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/*
	 * Set changes in Objects
	 */
	public void onStateChanged(StateChangeEvent event){
		setting.setCenter(event.getCenter());
		setting.setZoom(event.getZoomLevel());
	}
	
	/*
	 * Update current location of yours and your friends
	 */
	public void onPoll(){
		log.info("update current locations is about to start");
		try{
			//set your current location
			CurrentLocation location = currentLocationRepository.findByUserId( session.getCurrentUser().getId() );
			currentMarker.setLatlng( new LatLng( location.getLatitude(), location.getLongitude() ));
			session.getCurrentUser().setCurrentLocation(location);
			
			//set your friends location
			List<User>users = new ArrayList<User>();
			for(Friend f : friends)
				users.add(f.getUser());
			List<CurrentLocation> friendsLocation = currentLocationRepository.findByUsersId(friendsMarker.keySet());
			for(CurrentLocation cl : friendsLocation){
				friendsMarker.get(cl.getUser().getId()).setLatlng( new LatLng( cl.getLatitude(), cl.getLongitude() ));
				friendsMarker.get(cl.getUser().getId()).setTitle( cl.getUser().getLogin() + " [ " + cl.getDate() + " ]");
				for(Friend f : friends)
					if(f.getFriend().getId() == cl.getUser().getId())
						f.getFriend().setCurrentLocation(cl);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		log.info("after update current locations");
	}
	
	/*
	 * Send invitation to user
	 */
	public void onSendInvitation(ActionEvent event){
		try{
			for(Friend f : friends){
				if(f.getFriend().getLogin().equalsIgnoreCase(friendNick))
					throw new FriendArleadyExist("ten uzytkownik juz jest na liscie");
			}
			friendService.sendInvitation(session.getCurrentUser(), friendNick);
			throw new FriendArleadyExist(friendNick + " jest juz dodany do listy " + session.getUserLogin());
		}catch(IllegalArgumentException iae){
			
		}catch(FriendArleadyExist fae){
			log.info(fae.getMessage());
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, friendNick + " jest juz na twoiej liscie przyjaciol ",  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Display location in pass
	 */
	public void onRowSelect(SelectEvent event){
		Location location = (Location)event.getObject();
		
		if(oldMarker == null){
			oldMarker = new Marker(new LatLng( location.getLatitude(), location.getLongitude() ), "Twoja stara pozycja [ " + location.getDate() + " ]");
			setting.setCenter(new LatLng( location.getLatitude(), location.getLongitude() ));
			map.addOverlay(oldMarker);
		}else{
			oldMarker.setLatlng(new LatLng( location.getLatitude(), location.getLongitude() ));
			setting.setCenter(new LatLng( location.getLatitude(), location.getLongitude() ));
		}
	}
	
	/*
	 * Display friend location
	 */
	public void onRowSelectFriend(SelectEvent event){
		Friend f = (Friend)event.getObject();
		setting.setCenter(new LatLng(f.getFriend().getCurrentLocation().getLatitude(), f.getFriend().getCurrentLocation().getLongitude()));
	}
	
	/*
	 * Select marker on the map
	 */
    public void onMarkerSelect(OverlaySelectEvent event) {
    	if(event.getOverlay() instanceof Marker){
            Marker marker = (Marker) event.getOverlay();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Selected", marker.getTitle()));
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

	public String getFriendNick() {
		return friendNick;
	}

	public void setFriendNick(String friendNick) {
		this.friendNick = friendNick;
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
	
}