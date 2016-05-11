package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

import com.pw.lokalizator.controller.GoogleMapController.GoogleMapControllerModes;
import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.model.GoogleMapTypes;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="location")
@ViewScoped
public class LocationViewController implements Serializable{
	private Logger LOG = Logger.getLogger(LocationViewController.class);
	
	@EJB
	private LocationRepository locationRepository;
	@EJB
	private UserRepository userRepository;
	
	private MapModel model = new DefaultMapModel();
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private LokalizatorSession session;
	

    private Location selectedUserLocation;
	private String selectedUserLoginToShow;
	private User selectedUser;
	
	
	private String selectedUserToFollow;
	private Map<String, User>followUserList;
   
	@PostConstruct
	private void postConstruct(){
		
	}
	
	
	public List<String> onAutoCompleteUser(String userLogin){
		List<User>userList = userRepository.findByLoginLike(userLogin);
		
		return userList.stream()
		        .map(u -> u.getLogin())
		        .collect(Collectors.toList());
	}

	public void onUserSelectToFollow(){
		if(!isUserFollow(selectedUserToFollow)){
			User user = userRepository.findByLogin(selectedUserToFollow);
			googleMapController.addUser(user);
			
			//TEST
			googleMapController.update();
		} else {
			JsfMessageBuilder.errorMessageFromProperties("userArleadyFollowed");
		}
	}
	
	public boolean isUserFollow(String login){
		for(User user : googleMapController.getUsersToFollow())
			if(user.getLogin().equals(login))
				return true;
		
		return false;
	}

    public void onTabChange(TabChangeEvent event) {
    	System.out.println(event.getTab().getTitle());
    	googleMapController.setMode(GoogleMapControllerModes.FOLLOW_USERS);
    }
	
	
	/*
	public void onUserSelectToDisplay(){
		if(!selectedUserLoginToDisplay.equals(lastUserLoginToDisplay)){
			
			if(!isUserFollow(selectedUserLoginToDisplay)){
				selectedUserLocationList = locationRepository.getLastUserLocationByLogin(selectedUserLoginToDisplay);
			//	googleMapController.addOverlays(selectedUserLocationList);

			} else {
			//	List<Marker>markers = googleMapController.getUserOverlays(Marker.class, selectedUserLoginToDisplay, null);
			//    selectedUserLocationList = getLocationsFromMarkers(markers);
			}
			
			lastUserLoginToDisplay = selectedUserLoginToDisplay;
		}
	}
	
	private List<Location>getLocationsFromMarkers(List<Marker>markers){
		List<Location>locations = new ArrayList<>();
		
		for(Marker marker : markers){
			locations.add((Location)marker.getData());
		}
		
		return locations;
	}
	

	
	/**
	 * Usuwa lokacje ostatniego uzytkownika ktory byl wyswietlonya nie jest na liscie do sledzenia
	 * @param login
	 */
	/*
	private void removeUserLocationToDisplay(String login){
		if(!isUserFollow(login)){
			//GoogleMapModel googleMap = googleMapController.getGoogleMapModel();
			//googleMapController.removeUserOverlays(Marker.class, login, null);
			//googleMapController.removeUserOverlays(Circle.class, login, null);
		}
	}
	
	public void onUserLocationRowSelect(SelectEvent event){
		Location location = (Location) event.getObject();
		googleMapController.setCenter(location.getLatitude(), location.getLongitude());
	}

	private void setGoogleMapCenter(LatLng center){
		//TODO
	}
	
	/*
	 * Find your location in pass
	 */
	public void findLocations(ActionEvent actionEvent){

	}
	

	/*
	 * Update current location of yours and your friends
	 */
	public void onPoll(){

	}
	
	/*
	 * Send invitation to user
	 */
	public void onSendInvitation(ActionEvent event){

	}
	
	/*
	 * Display location in pass
	 */
	public void onRowSelect(SelectEvent event){

	}
	
	/*
	 * Select marker on the map
	 */
    public void onMarkerSelect(OverlaySelectEvent event) {

    }


	public Location getSelectedUserLocation() {
		return selectedUserLocation;
	}


	public void setSelectedUserLocation(Location selectedUserLocation) {
		this.selectedUserLocation = selectedUserLocation;
	}


	public String getSelectedUserLoginToShow() {
		return selectedUserLoginToShow;
	}


	public void setSelectedUserLoginToShow(String selectedUserLoginToShow) {
		this.selectedUserLoginToShow = selectedUserLoginToShow;
	}


	public User getSelectedUser() {
		return selectedUser;
	}


	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}


	public String getSelectedUserToFollow() {
		return selectedUserToFollow;
	}


	public void setSelectedUserToFollow(String selectedUserToFollow) {
		this.selectedUserToFollow = selectedUserToFollow;
	}
    
    
}
