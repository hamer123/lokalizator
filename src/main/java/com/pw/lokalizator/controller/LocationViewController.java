package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.map.Overlay;
import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.GoogleMapComponentVisible;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.repository.CellInfoMobileRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.repository.WifiInfoRepository;
import com.pw.lokalizator.serivce.qualifier.DialogGoogleMap;
import com.pw.lokalizator.service.GoogleMapUserComponentService;
import com.pw.lokalizator.service.UserLastLocationsService;
import com.pw.lokalizator.singleton.RestSessionManager;


@ViewScoped
@Named(value="location")
public class LocationViewController implements Serializable{
	@Inject
	private UserRepository userRepository;
	@Inject
	private WifiInfoRepository wifiInfoRepository;
	@Inject
	private CellInfoMobileRepository cellInfoMobileRepository;
	
	@Inject
	private GoogleMapController googleMapController;
	@Inject @DialogGoogleMap
	private GoogleMapSingleUserDialogController googleMapSingleUserDialogController;
	
	@Inject
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject
	private UserLastLocationsService userLastLocationsService;

	@Inject
	RestSessionManager restSessionManager;
	@Inject
	Logger logger;
	
	private static final String GOOGLE_MAP_STYLE_MIN_WIDTH = "googleMapMin";
	private static final String GOOGLE_MAP_STYLE_MAX_WIDTH = "googleMapMax";
	
	private String googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	private String login = "";
	private Location selectLocation;
	private Location locationToDisplayDetails;
	private User selectUser = null;
	private Set<User>users = new HashSet<User>();
	private GoogleMapComponentVisible googleMapVisible;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  ACTIONS   ////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostConstruct
	public void init(){
		googleMapVisible = new GoogleMapComponentVisible();
		googleMapVisible.setCircleGps(true);
		googleMapVisible.setCircleNetworkNasz(true);
		googleMapVisible.setCircleNetworkObcy(true);
		googleMapVisible.setMarkerGps(true);
		googleMapVisible.setMarkerNetworkNasz(true);
		googleMapVisible.setMarkerNetworkObcy(true);
		googleMapVisible.setPolygon(false);
	}
	
	public void onAddUserToFollow(){
		try{
			
			if(isUserArleadyOnList(login)){
				JsfMessageBuilder.errorMessage("Użytkownik jest już na liście");
				return;
			}
			
			User user = userRepository.findByLoginFetchArea(login);
			List<Overlay>overlays = googleMapUserComponentService.lastLocation(user, googleMapVisible);
			googleMapController.addOverlay(overlays);
			users.add(user);
			
			JsfMessageBuilder.infoMessage("Udało się dodać uzytkownika do śledzenia");
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Błąd przy próbie dodania użytkownika");
			logger.error(e);
		}
	}

	public void onPoll(){
		try{
			for(User user : users){
				if(restSessionManager.isUserOnline(user.getLogin()))
					userLastLocationsService.updateUserLastLocation(user);
			}
			
			if(!users.isEmpty()){
				List<Overlay>overlays = googleMapUserComponentService.lastLocation(users, googleMapVisible);
				googleMapController.replace(overlays);	
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się odnowić lokalizacji");
			logger.equals(e);
		}
	}
	
	public void onRemoveUserFromFollow(User user){
		try{
			removeUserFromList(user);
			removeUserFromGoogleMap(user);
			JsfMessageBuilder.infoMessage("Udało się usunąć uzytkownika z listy śledzenia");
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się usunąć uzytkownika z listy śledzenia");
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public void onChangeSetting(){
		List<Overlay>overlays = googleMapUserComponentService.lastLocation(users, googleMapVisible);
		googleMapController.replace(overlays);
	}
	
	public void onShowLocation(){
		String center = GoogleMapModel.center(selectLocation);
		googleMapController.setCenter(center);
	}
	
	public List<String> onAutocompleteLogin(String login){
		List<String>logins = userRepository.findLoginByLoginLike(login);
		return filterLogins(logins);
	}
	
	public List<String> usersOnline(){
		return restSessionManager.getUserOnlineLogins();
	}

	public void onShowUserLastLocations(User user){
		selectUser = user;
	}
	
	public void onToggleMainPanel(ToggleEvent event){
		if(googleMapStyle.equals(GOOGLE_MAP_STYLE_MIN_WIDTH))
			googleMapStyle = GOOGLE_MAP_STYLE_MAX_WIDTH;
		else
			googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	}
	
	public void onShowPolygonLocation(Area area){
		AreaPoint areaPoint = area.getPoints().get(1);
		String center = GoogleMapModel.center(areaPoint.getLat(), areaPoint.getLng());
		googleMapController.setCenter(center);
	}
	
	public void onSetLocationToDipslayDetails(Location location){
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			locationNetwork.setWifiInfo( wifiInfoRepository.findByLocationId(location.getId()) );
			locationNetwork.setCellInfoMobile( cellInfoMobileRepository.findByLocationId(location.getId()) );
		}
		
		locationToDisplayDetails = location;
	}
	
	public void onShowOnlineUserLastLocations(String login){
		User user = userRepository.findByLogin(login);
		List<Overlay>overlays = googleMapUserComponentService.lastLocation(user, GoogleMapComponentVisible.NO_POLYGON);
		googleMapSingleUserDialogController.replace(overlays);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  UTILITIS  /////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean isUserArleadyOnList(String login){
		for(User user : users)
			if(user.getLogin().equals(login))
				return true;
		
		return false;
	}
	
	private <T> void addToListIfNotNull(List<T>list, T obj){
		if(obj != null)
			list.add(obj);
	}
	
	private List<String> filterLogins(List<String>logins){
		List<String> filter = getLogins();
		return logins.stream()
				     .filter(s -> !(filter.contains(s)))
				     .collect(Collectors.toList());
	}
	
	private void removeUserFromList(User user){
		cleanSelectedUserIfUserEquals(user);
		users.remove(user);
	}
	
	private void cleanSelectedUserIfUserEquals(User user){
		if(selectUser != null && selectUser.equals(user))
			selectUser = null;
	}
	
	private void removeUserFromGoogleMap(User user){
		OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().login(user.getLogin())
                                                                              .build();
		
		googleMapController.removeOverlay(identyfikator);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<String>getLogins(){
		return users.stream()
		            .map(u -> u.getLogin())
		            .collect(Collectors.toList());
	}
	

	public List<Location> selectUserLocations(){
		List<Location>locations = new ArrayList<Location>();
		
		addToListIfNotNull(locations, selectUser.getLastLocationGPS());
		addToListIfNotNull(locations, selectUser.getLastLocationNetworkNaszaUsluga());
		addToListIfNotNull(locations, selectUser.getLastLocationNetworObcaUsluga());
		
		return locations;
	}
	
	public LocalizationServices getLocalizationServices(Location location){
		if(location instanceof LocationNetwork)
			return ( (LocationNetwork)location ).getLocalizationServices();
		
		return null;
	}

	public GoogleMapSingleUserDialogController getGoogleMapSingleUserDialogController() {
		return googleMapSingleUserDialogController;
	}

	public Location getLocationToDisplayDetails() {
		return locationToDisplayDetails;
	}
	
	public Set<User> getUsers() {
		return users;
	}

	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public User getSelectUser() {
		return selectUser;
	}

	public void setSelectUser(User selectUser) {
		this.selectUser = selectUser;
	}

	public Location getSelectLocation() {
		return selectLocation;
	}

	public void setSelectLocation(Location selectLocation) {
		this.selectLocation = selectLocation;
	}

	public GoogleMapComponentVisible getGoogleMapVisible() {
		return googleMapVisible;
	}

	public String getGoogleMapStyle() {
		return googleMapStyle;
	}
	
}
