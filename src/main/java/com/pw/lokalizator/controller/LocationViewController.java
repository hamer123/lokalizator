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

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.model.OverlayVisibility;
import com.pw.lokalizator.model.entity.CellInfoGSM;
import com.pw.lokalizator.model.entity.CellInfoLte;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.GoogleMaps;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.CellInfoMobileRepository;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.repository.WifiInfoRepository;
import com.pw.lokalizator.singleton.RestSessionManager;

@Named(value="location")
@ViewScoped
public class LocationViewController implements Serializable{
	private Logger logger = Logger.getLogger(LocationViewController.class);
	@EJB
	private UserRepository userRepository;
	@EJB
	private RestSessionManager restSessionManager;
	@EJB
	private CellInfoMobileRepository cellInfoMobileRepository;
	@EJB
	private WifiInfoRepository wifiInfoRepository;
	@Inject
	private GoogleMapControllerFollowMode googleMapControllerFollowMode;
	@Inject
	private GoogleMapControllerSingleMode googleMapControllerSingleMode;
	
	private static final Providers[] providers = Providers.values();
	private static final LocalizationServices[] services = LocalizationServices.values();
	
	private boolean panelDaneVisible;
	private User selectedUserToShowData;
	private String selectedLoginToFollow;
	private Location selectedUserToShowDataLocation;
	private List<String>userLoginOnline;
	
	private LocationNetwork locationNetworkShowSzczegoly;
	private CellInfoGSM cellInfoGSMShowSzczegoly;
	private CellInfoLte cellInfoLteShowSzczegoly;
	
	private GoogleMaps googleMapType;
	private boolean streetView;
	private GoogleMapMode googleMapMode;
	
	
	private Providers choicedProvider;
	private LocalizationServices choicedLocalizationServices;
	private OverlayVisibility overlayVisibilityFollow;
	
	private enum GoogleMapMode{
		FOLLOW_USER,SINGLE_USER;
	}
	
	@PostConstruct
	private void postConstruct(){
		choicedProvider = providers[0];
		choicedLocalizationServices = services[0];
		overlayVisibilityFollow = googleMapControllerFollowMode.getGpsVisibility();
		
		
		googleMapType = GoogleMaps.HYBRID;
		streetView = true;
		panelDaneVisible = false;
		googleMapMode = GoogleMapMode.FOLLOW_USER;
		
		userLoginOnline = restSessionManager.getUserOnlineLogins();
	}
	
	
	public void test(){
		System.out.println(overlayVisibilityFollow);
	}
	
	public void onChooseProviderChange(){
		if(choicedProvider == Providers.GPS)
			overlayVisibilityFollow = googleMapControllerFollowMode.getGpsVisibility();
		else 
			overlayVisibilityFollow = googleMapControllerFollowMode.getNetworkNaszaUslugaVisibility();
	}
	
	public void onChooseLocatiozacionServiceChange(){
		if(choicedLocalizationServices == LocalizationServices.NASZ)
			overlayVisibilityFollow = googleMapControllerFollowMode.getNetworkNaszaUslugaVisibility();
		else
			overlayVisibilityFollow = googleMapControllerFollowMode.getNetworkObcaUslugaVisibilty();
	}
	
	public List<String> onAutoCompleteUser(String userLogin){
		return userRepository.findLoginByLoginLike(userLogin);
	}

	public void onUserSelectToFollow(){
		if(!isUserFollow(selectedLoginToFollow))
			googleMapControllerFollowMode.addUser(selectedLoginToFollow);
		else 
			JsfMessageBuilder.errorMessageFromProperties("userArleadyFollowed");
	}
	
	public void onUserRemove(String login){
		googleMapControllerFollowMode.removeUser(login);
		
		if(selectedUserToShowData != null && login.equals( selectedUserToShowData.getLogin() )){
			clearAndHideDanePanel();
		}
	}
	
	public void onRowSelectedOstatnieLokacje(SelectEvent event){
		Location location = (Location) event.getObject();
		String center = googleMapControllerFollowMode.createCenter(location.getLatitude(), location.getLongitude());
		googleMapControllerFollowMode.setCenter(center);
		googleMapControllerFollowMode.setZoom(15);
	}

	private void clearAndHideDanePanel(){
		selectedUserToShowData = null;
		panelDaneVisible = false;
	}
	
	public void onPokazDane(String login){
		selectedUserToShowData = googleMapControllerFollowMode.getUser(login);
		panelDaneVisible = true;
	}
	
	public boolean isUserFollow(String login){
		for(User user : googleMapControllerFollowMode.getUsersToFollow())
			if(user.getLogin().equals(login))
				return true;
		
		return false;
	}

    public void onTabChange(TabChangeEvent event) {
    	String tabTitle = event.getTab().getTitle();
    	
    	if(tabTitle.equals("Aktualne Lokacje"))
    		googleMapMode = GoogleMapMode.FOLLOW_USER;
    	else if(tabTitle.equals("Historia Lokacji"))
    		googleMapMode = GoogleMapMode.SINGLE_USER;
    }

    public List<Location>getSelectedUserToShowDataCurrentLocations(){
    	List<Location>locations = new ArrayList<Location>();
    	
		if(selectedUserToShowData.getLastLocationGPS() != null)
			locations.add(selectedUserToShowData.getLastLocationGPS());
		if(selectedUserToShowData.getLastLocationNetworObcaUsluga() != null)
			locations.add(selectedUserToShowData.getLastLocationNetworObcaUsluga());
		if(selectedUserToShowData.getLastLocationNetworkNaszaUsluga() != null)
		    locations.add(selectedUserToShowData.getLastLocationNetworkNaszaUsluga());
    	
    	return locations;
    }
    
    public void onShowSzczegoly(Location location){
    	clearLocationSzczegoly();
    	setupLocationSzczegoly(location);
    }
    
    public String getLocalizationServices(Location location){
    	LocationNetwork locationNetwork = (LocationNetwork) location;
    	return locationNetwork.getLocalizationServices().toString();
    }
    
    private void setupLocationSzczegoly(Location location){
    	if(location instanceof LocationNetwork)
    		setupLocationNetworkSzczegoly(location);
    }
    
    private void setupLocationNetworkSzczegoly(Location location){
    	locationNetworkShowSzczegoly = (LocationNetwork)location;
    	
    	locationNetworkShowSzczegoly.setInfoWifi( wifiInfoRepository.findByLocationId(location.getId()) );
    	locationNetworkShowSzczegoly.setCellInfoMobile( cellInfoMobileRepository.findByLocationId(location.getId()) );
    	
    	if(locationNetworkShowSzczegoly.getCellInfoMobile() instanceof CellInfoGSM)
    		cellInfoGSMShowSzczegoly = (CellInfoGSM) locationNetworkShowSzczegoly.getCellInfoMobile();
    	else if(locationNetworkShowSzczegoly.getCellInfoMobile() instanceof CellInfoLte)
    		cellInfoLteShowSzczegoly = (CellInfoLte) locationNetworkShowSzczegoly.getCellInfoMobile();
    }
    
    private void clearLocationSzczegoly(){
    	cellInfoGSMShowSzczegoly = null;
    	cellInfoLteShowSzczegoly = null;
    	locationNetworkShowSzczegoly = null;
    }
	
	public void onPollFollowMode(){
		googleMapControllerFollowMode.update();
		userLoginOnline = restSessionManager.getUserOnlineLogins();
	}
	
    public void onMarkerSelect(OverlaySelectEvent event) {

    }
    
    //
    //
    //
    
	public CellInfoGSM getCellInfoGSMShowSzczegoly() {
		return cellInfoGSMShowSzczegoly;
	}

	public void setCellInfoGSMShowSzczegoly(CellInfoGSM cellInfoGSMShowSzczegoly) {
		this.cellInfoGSMShowSzczegoly = cellInfoGSMShowSzczegoly;
	}

	public CellInfoLte getCellInfoLteShowSzczegoly() {
		return cellInfoLteShowSzczegoly;
	}

	public void setCellInfoLteShowSzczegoly(CellInfoLte cellInfoLteShowSzczegoly) {
		this.cellInfoLteShowSzczegoly = cellInfoLteShowSzczegoly;
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

	public LocationNetwork getLocationNetworkShowSzczegoly() {
		return locationNetworkShowSzczegoly;
	}

	public void setLocationNetworkShowSzczegoly(
			LocationNetwork locationNetworkShowSzczegoly) {
		this.locationNetworkShowSzczegoly = locationNetworkShowSzczegoly;
	}

	public GoogleMaps getGoogleMapType() {
		return googleMapType;
	}

	public void setGoogleMapType(GoogleMaps googleMapType) {
		this.googleMapType = googleMapType;
	}

	public boolean isStreetView() {
		return streetView;
	}

	public void setStreetView(boolean streetView) {
		this.streetView = streetView;
	}

	public GoogleMapMode getGoogleMapMode() {
		return googleMapMode;
	}

	public Providers[] getProviders() {
		return providers;
	}

	public Providers getChoicedProvider() {
		return choicedProvider;
	}

	public void setChoicedProvider(Providers choicedProvider) {
		this.choicedProvider = choicedProvider;
	}

	public LocalizationServices[] getServices() {
		return services;
	}

	public LocalizationServices getChoicedLocalizationServices() {
		return choicedLocalizationServices;
	}

	public void setChoicedLocalizationServices(
			LocalizationServices choicedLocalizationServices) {
		this.choicedLocalizationServices = choicedLocalizationServices;
	}

	public OverlayVisibility getOverlayVisibilityFollow() {
		return overlayVisibilityFollow;
	}

}
