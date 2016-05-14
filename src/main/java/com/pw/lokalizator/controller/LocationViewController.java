package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.sql.Update;
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
import com.pw.lokalizator.singleton.RestSessionManager;

@Named(value="location")
@ViewScoped
public class LocationViewController implements Serializable{
	private Logger LOG = Logger.getLogger(LocationViewController.class);
	@EJB
	private LocationRepository locationRepository;
	@EJB
	private UserRepository userRepository;
	@EJB
	private RestSessionManager restSessionManager;
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private LokalizatorSession session;
	
	private boolean panelDaneVisible;
	private User selectedUserToShowData;
	private String selectedLoginToFollow;
	private Location selectedUserToShowDataLocation;
	private List<String>userLoginOnline;
   
	@PostConstruct
	private void postConstruct(){
		userLoginOnline = restSessionManager.getUserOnlineLogins();
		panelDaneVisible = false;
	}
	
	public List<String> onAutoCompleteUser(String userLogin){
		return userRepository.findLoginByLoginLike(userLogin);
	}

	public void onUserSelectToFollow(){
		if(!isUserFollow(selectedLoginToFollow))
			googleMapController.addUser(selectedLoginToFollow);
		else 
			JsfMessageBuilder.errorMessageFromProperties("userArleadyFollowed");
	}
	
	public void onUserRemove(String login){
		googleMapController.removeUser(login);
		
		if(login.equals(selectedUserToShowData.getLogin())){
			clearAndHideDanePanel();
		}
	}
	
	public void onRowSelectedOstatnieLokacje(SelectEvent event){
		Location location = (Location) event.getObject();
		String center = googleMapController.createCenter(location.getLatitude(), location.getLongitude());
		googleMapController.setCenter(center);
		googleMapController.setZoom(15);
	}

	private void clearAndHideDanePanel(){
		selectedUserToShowData = null;
		panelDaneVisible = false;
	}
	
	public void onPokazDane(String login){
		System.out.println(login);
		selectedUserToShowData = googleMapController.getUser(login);
		
		panelDaneVisible = true;
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

    public List<Location>getSelectedUserToShowDataCurrentLocations(){
    	List<Location>locations = new ArrayList<Location>();
    	
    	locations.add(selectedUserToShowData.getLastGpsLocation());
    	locations.add(selectedUserToShowData.getLastNetworkLocation());
    	locations.add(selectedUserToShowData.getLastOwnProviderLocation());
    	
    	return locations;
    }
    
    
	public void findLocations(ActionEvent actionEvent){

	}
	

	public void onPoll(){
		googleMapController.update();
		userLoginOnline = restSessionManager.getUserOnlineLogins();
	}
	
	public void onSendInvitation(ActionEvent event){

	}
	
    public void onMarkerSelect(OverlaySelectEvent event) {

    }
    
	public User getSelectedUserToShowData() {
		return selectedUserToShowData;
	}

	public void setSelectedUserToShowData(User selectedUserToShowData) {
		this.selectedUserToShowData = selectedUserToShowData;
	}

	public String getSelectedLoginToFollow() {
		return selectedLoginToFollow;
	}

	public void setSelectedLoginToFollow(String selectedLoginToFollow) {
		this.selectedLoginToFollow = selectedLoginToFollow;
	}

	public boolean isPanelDaneVisible() {
		return panelDaneVisible;
	}


	public void setPanelDaneVisible(boolean panelDaneVisible) {
		this.panelDaneVisible = panelDaneVisible;
	}

	public Location getSelectedUserToShowDataLocation() {
		return selectedUserToShowDataLocation;
	}

	public void setSelectedUserToShowDataLocation(
			Location selectedUserToShowDataLocation) {
		this.selectedUserToShowDataLocation = selectedUserToShowDataLocation;
	}

	public List<String> getUserLoginOnline() {
		return userLoginOnline;
	}
  
}
