package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.RouteManager;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.google.map.GoogleMapComponentVisible;
import com.pw.lokalizator.model.google.map.GoogleMapModel;
import com.pw.lokalizator.model.session.LokalizatorSession;
import com.pw.lokalizator.model.google.component.GoogleLocation;
import com.pw.lokalizator.model.google.component.Route;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.repository.CellInfoMobileRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.repository.WifiInfoRepository;
import com.pw.lokalizator.serivce.qualifier.DialogUserLocationGoogleMap;
import com.pw.lokalizator.serivce.qualifier.UserGoogleMap;
import com.pw.lokalizator.service.GoogleMapUserComponentService;
import com.pw.lokalizator.singleton.RestSessionManager;

@ViewScoped
@Named(value="location")
public class LocationController implements Serializable{
	private static final long serialVersionUID = -5534429129019431383L;
	@Inject @UserGoogleMap
	private GoogleMapController googleMapController;
	@Inject @DialogUserLocationGoogleMap
	private DialogUserLocationGoogleMapController googleMapSingleUserDialogController;
	@Inject
	private UserRepository userRepository;
	@Inject
	private WifiInfoRepository wifiInfoRepository;
	@Inject
	private CellInfoMobileRepository cellInfoMobileRepository;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private LokalizatorSession lokalizatorSession;
	@Inject
	private Logger logger;
	
	static final String GOOGLE_MAP_STYLE_MIN_WIDTH = "googleMapMin";
	static final String GOOGLE_MAP_STYLE_MAX_WIDTH = "googleMapMax";
	static final long ONE_MINUTE = 1000 * 60;
	
	private String googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	private String login = "";
	private Location selectLocation;
	private Location locationToDisplayDetails;
	private User selectUserForLastLocations;
	private User selectUserForUserData = null;
	private Map<String,User>users = new HashMap<String,User>();
	private RouteManager gpsRouteManager = new RouteManager();
	private RouteManager networkNaszRouteManager = new RouteManager();
	private RouteManager networkObcyRouteManager = new RouteManager();
	private Set<Long>activeAreaIds = new HashSet<>();
	private boolean checkAreaEvent;
	private boolean createRoutes;
	private GoogleMapComponentVisible googleMapVisible;
	private UserViewSettingDialog userViewSettingDialog;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  ACTIONS   ////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostConstruct
	public void init(){
		initdefaultGoogleMapVisibility();
		googleMapController.setDisplayMessageOnSelectOverlay(true);
		activeAreaIds.addAll( areaRepository.findIdByProviderIdAndActive(lokalizatorSession.getUser().getId(), true) );
		checkAreaEvent = true;
		createRoutes = true;
	}

	/**
	 * Dodaje uzytkonwika do listy sledzenia
	 * tworzymy komponenety i dodajemy do google map
	 * tworzymy managerow do zarzadzania tworzeniem trasy sledzenia
	 */
	public void onAddUserToFollow(){
		try{
			if(isUserArleadyOnList(login)){
				JsfMessageBuilder.errorMessageBundle("user.already.on.follow.list");
				return;
			}
			User user = userRepository.findByLoginFetchArea(login);
			users.put(user.getLogin(),user);
			createAndAddComponentsToGoogleMap(user);
			addUserToRouteManagers(user);
			JsfMessageBuilder.infoMessageBundle("user.success.add.to.follow.list");
		} catch(Exception e){
			JsfMessageBuilder.errorMessageBundle("error.on.add.user.to.follow.list");
			e.printStackTrace();
		}
	}

	/**
	 *  Pobierz nowe lokacje sledzonych uzytknikow i zrenderuj google map
	 *  Jesli sa wlaczone powiadomienia Eventu Areny pokaz powiadomienie jesli takie istnieje
	 * */
	public void onPoll(){
		try{

			if(!users.isEmpty()){
				refresUsersLastLocations();
				renderGoogleMap();
			}

			if(isCheckAreaEvent()){
				List<AreaEvent>areaEvents = checkAreaEvent();
				for(AreaEvent areaEvent : areaEvents)
					JsfMessageBuilder.infoMessage(areaEvent.getMessage());
			}
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się odnowić lokalizacji");
			e.printStackTrace();
		}
	}
	
	public void onRemoveUserFromFollow(User user){
		try{
			removeUserFromFollow(user);
			removeUserComponentsFromGoogleMap(user);
			JsfMessageBuilder.infoMessage("Udało się usunąć uzytkownika z listy śledzenia");
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie udało się usunąć uzytkownika z listy śledzenia");
			e.printStackTrace();
		}
	}
	
	public void onChangeSetting(){
		if(!users.isEmpty())
			renderGoogleMap();
	}
	
	public void onShowLocation(){
		String center = GoogleMapModel.center(selectLocation);
		googleMapController.setCenter(center);
	}
	
	public List<String> onAutocompleteLogin(String login){
		List<String>logins = userRepository.findLoginByLoginLike(login);
		return filterLogins(logins);
	}
	
	public void onShowUserLastLocations(User user){
		selectUserForLastLocations = user;
	}
	
	public void onToggleMainPanel(ToggleEvent event){
		if(googleMapStyle.equals(GOOGLE_MAP_STYLE_MIN_WIDTH))
			googleMapStyle = GOOGLE_MAP_STYLE_MAX_WIDTH;
		else
			googleMapStyle = GOOGLE_MAP_STYLE_MIN_WIDTH;
	}
	
	public void onShowPolygonLocation(Area area){
		if( area.isAktywny() && !googleMapVisible.isActivePolygon()  || 
		    !area.isAktywny() && !googleMapVisible.isNotActivePolygon() ){
			 JsfMessageBuilder.infoMessage("Wpierw włacz widocznosc obszarow w ustawieniach !");
			 return;
		}
		
		AreaPoint areaPoint = area.getPoints().get(0);
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
		User user = userRepository.findByLogins(login);
		List<GoogleLocation> googleLocations = googleMapUserComponentService.lastLocations(user, GoogleMapComponentVisible.NO_POLYGON);
		
		googleMapSingleUserDialogController.clear();
		
		for(GoogleLocation googleLocation : googleLocations)
			googleMapSingleUserDialogController.addOverlay(googleLocation.overlays());
		
		if(!googleLocations.isEmpty())
			googleMapSingleUserDialogController.setCenterIfLocationExist(googleLocations.get(0).getMarker());
	}
	
	public void onChangeCreateRoutes(){
		if(isCreateRoutes()){
			for(User user : users.values())
				addUserToRouteManagers(user);
		} else {
			gpsRouteManager.clear();
			networkNaszRouteManager.clear();
			networkObcyRouteManager.clear();
		}
		renderGoogleMap();
	}
	
	public void onEditUserSetting(User user){
		userViewSettingDialog = new UserViewSettingDialog(user.getLogin());
	}

	public void onClickUserToDisplayData(User user){
		selectUserForUserData = user;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  UTILITIS  /////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Location locationFromLastSelectedOverlay(){
		Overlay overlay = googleMapController.getLastSelectedOverlay();
		if(overlay instanceof Marker)
			return (Location) overlay.getData();
		return null;
	}
	
	void updateRoutesPaths(){
		for(User user : users.values()){
			String login = user.getLogin();
			if(restSessionManager.isUserOnline(login)){
				Location location = user.getLastLocationGPS();
				Route route = gpsRouteManager.find(login);
				if(validateLocationToUpdateRoute(route, location))
					gpsRouteManager.update(login, location);
				
				location = user.getLastLocationNetworkNaszaUsluga();
				route = networkNaszRouteManager.find(login);
				if(validateLocationToUpdateRoute(route, location))
					networkNaszRouteManager.update(login, location);
				
				location = user.getLastLocationNetworObcaUsluga();
				route = networkObcyRouteManager.find(login);
				if(validateLocationToUpdateRoute(route, location))
					networkObcyRouteManager.update(login, location);
			}
		}
	}

	boolean validateLocationToUpdateRoute(Route route, Location location){
		return location != null;
//			   !route.getLastLocation().getDate().equals(location.getDate());
	}
	
	void addRoutesToGoogleMap(){
		if(googleMapVisible.isGpsRoute()){
			for(Route route : gpsRouteManager.getVisible())
				googleMapController.addOverlay(route.overlays());
		}
		
		if(googleMapVisible.isNetworkNaszRoute()){
			for(Route route : networkNaszRouteManager.getVisible())
				googleMapController.addOverlay(route.overlays());
		}
		
		if(googleMapVisible.isNetworkObcyRoute()){
			for(Route route : networkObcyRouteManager.getVisible())
				googleMapController.addOverlay(route.overlays());
		}
	}

	boolean isUserArleadyOnList(String login){
		for(String key : users.keySet())
			if(key.equals(login))
				return true;
		
		return false;
	}
	
	<T> void addToListIfNotNull(List<T>list, T obj){
		if(obj != null)
			list.add(obj);
	}
	
	List<String> filterLogins(List<String>logins){
		List<String> filter = logins();
		return logins.stream()
				     .filter(s -> !(filter.contains(s)))
				     .collect(Collectors.toList());
	}
	
	void removeUserFromFollow(User user){
		if(user.equals(selectUserForLastLocations)) 
			selectUserForLastLocations = null;
		users.remove(user.getLogin());
	}
	
	void removeUserComponentsFromGoogleMap(User user){
		OverlayIdentyfikator identyfikator = 
			new OverlayIdentyfikatorBuilder()
		   .login(user.getLogin())
           .build();
		googleMapController.removeOverlay(identyfikator);
	}
	
	void refresUsersLastLocations(){
		for(User user : users.values()){
			if(restSessionManager.isUserOnline(user.getLogin()))
				refreshLastLocations(user);
		}
	}
	
	void refreshLastLocations(User user){
		User _user = userRepository.findById(user.getId());
		user.setLastLocationGPS(_user.getLastLocationGPS());
		user.setLastLocationNetworkNaszaUsluga(_user.getLastLocationNetworkNaszaUsluga());
		user.setLastLocationNetworObcaUsluga(_user.getLastLocationNetworObcaUsluga());
	}
	
	List<Polygon> createPolygons(Collection<User>users){
		List<Polygon>polygons = new ArrayList<Polygon>();
		for(User user : users)
			polygons.addAll( createPolygon(user) );
		return polygons;
	}
	
	void renderGoogleMap(){
		googleMapController.clear();
		
		List<GoogleLocation> googleLocations = googleMapUserComponentService.lastLocations(new HashSet<>(users.values()), googleMapVisible);
		addPositionToGoogleMap(googleLocations);
		List<Polygon>polygons = createPolygons(users.values());
		addPolygonToGoogleMap(polygons);
		
		if(isCreateRoutes()){
			updateRoutesPaths();
			addRoutesToGoogleMap();
		}	
	}
	
	void addPositionToGoogleMap(List<GoogleLocation> googleLocations){
		for(GoogleLocation googleLocation : googleLocations)
			googleMapController.addOverlay(googleLocation.overlays());
	}
	
	void addPolygonToGoogleMap(List<Polygon>polygons){
		for(Polygon polygon : polygons)
			googleMapController.addOverlay(polygon);
	}
	
	List<Polygon> createPolygon(User user){
		List<Polygon>polygons = new ArrayList<>();
		
		for(Area area : user.getAreas()){
			Polygon polygon = googleMapUserComponentService.polygon(area, googleMapVisible);
			if(polygon != null)
				polygons.add(polygon);
			}
		return polygons;
	}
	
	List<AreaEvent> checkAreaEvent(){
		List<AreaEvent>areaEvents = new ArrayList<>();
		Date from = new Date(new Date().getTime() - ONE_MINUTE);	
		for(long id : activeAreaIds){
			areaEvents.addAll( areaEventGPSRepository.findByAreaIdAndDate(id, from) );
			areaEvents.addAll( areaEventNetworkRepository.findByAreaIdAndDate(id, from) );
		}
		return areaEvents;
	}

	void createAndAddComponentsToGoogleMap(User user){
		List<GoogleLocation> googleLocations = googleMapUserComponentService.lastLocations(user, googleMapVisible);
		addPositionToGoogleMap(googleLocations);
		List<Polygon>polygons = createPolygon(user);
		addPolygonToGoogleMap(polygons);
	}
	
	public List<String> usersOnline(){
		return restSessionManager.getUserOnlineLogins();
	}
	
	public List<String>logins(){
		return users.values()
				    .stream()
		            .map(u -> u.getLogin())
		            .collect(Collectors.toList());
	}
	
	public List<Location> selectedUserLocations(){
		List<Location>locations = new ArrayList<Location>();
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationGPS());
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworkNaszaUsluga());
		addToListIfNotNull(locations, selectUserForLastLocations.getLastLocationNetworObcaUsluga());	
		return locations;
	}

	/**
	 * Dodaje uzytkownika do tras sledzenia
	 * @param user
	 */
	void addUserToRouteManagers(User user){
		if(user.getLastLocationGPS() != null)
			gpsRouteManager.add(user.getLogin(), user.getLastLocationGPS());
		if(user.getLastLocationNetworkNaszaUsluga() != null)
			networkNaszRouteManager.add(user.getLogin(), user.getLastLocationNetworkNaszaUsluga());
		if(user.getLastLocationNetworObcaUsluga() != null)
			networkObcyRouteManager.add(user.getLogin(), user.getLastLocationNetworObcaUsluga());
	}

	void initdefaultGoogleMapVisibility(){
		googleMapVisible = new GoogleMapComponentVisible();
		googleMapVisible.setCircleGps(true);
		googleMapVisible.setCircleNetworkNasz(true);
		googleMapVisible.setCircleNetworkObcy(true);
		googleMapVisible.setMarkerGps(true);
		googleMapVisible.setMarkerNetworkNasz(true);
		googleMapVisible.setMarkerNetworkObcy(true);
		googleMapVisible.setActivePolygon(true);
		googleMapVisible.setGpsRoute(true);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  NESTED CLASS  //////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public class UserViewSettingDialog{
		private Route gpsRoute;
		private Route networkNaszRoute;
		private Route networkObcyRoute;
		private String gpsRouteColor;
		private String networkNaszColor;
		private String networkObcyColor;
		private boolean disableGps = false;
		private boolean disableNetworkNasz = false;
		private boolean isDisableNetworkObcy = false;
		
		private UserViewSettingDialog(String login){
			gpsRoute = gpsRouteManager.find(login);
			networkNaszRoute = networkNaszRouteManager.find(login);
			networkObcyRoute = networkObcyRouteManager.find(login);

			if(gpsRoute == null)
				disableGps = true;
			else
				gpsRouteColor = gpsRoute.getPolyline().getStrokeColor();

			if(networkNaszRoute == null)
				disableNetworkNasz = true;
			else
				networkNaszColor = networkNaszRoute.getPolyline().getStrokeColor();

			if(networkObcyRoute == null)
				isDisableNetworkObcy = true;
			else
				networkObcyColor = networkObcyRoute.getPolyline().getStrokeColor();
		}

		public Route getGpsRoute() {
			return gpsRoute;
		}
		public Route getNetworkNaszRoute() {
			return networkNaszRoute;
		}
		public Route getNetworkObcyRoute() {
			return networkObcyRoute;
		}
		
		public String getGpsRouteColor() {
			return gpsRouteColor;
		}

		public void setGpsRouteColor(String gpsRouteColor) {
			this.gpsRouteColor = gpsRouteColor;
		}

		public String getNetworkNaszColor() {
			return networkNaszColor;
		}

		public void setNetworkNaszColor(String networkNaszColor) {
			this.networkNaszColor = networkNaszColor;
		}

		public String getNetworkObcyColor() {
			return networkObcyColor;
		}

		public void setNetworkObcyColor(String networkObcyColor) {
			this.networkObcyColor = networkObcyColor;
		}

		public void onSaveChangeSetting(){
			if(!isDisableGps())
				gpsRoute.getPolyline().setStrokeColor(gpsRouteColor.startsWith("#") ? gpsRouteColor : ("#" + gpsRouteColor));
			if(!isDisableNetworkNasz())
				networkNaszRoute.getPolyline().setStrokeColor(networkNaszColor.startsWith("#") ? networkNaszColor : ("#" + networkNaszColor));
			if(!isDisableNetworkObcy)
				networkObcyRoute.getPolyline().setStrokeColor(networkObcyColor.startsWith("#") ? networkObcyColor : ("#" + networkObcyColor));

			renderGoogleMap();
		}

		public boolean isDisableGps() { return disableGps; }

		public void setDisableGps(boolean disableGps) {this.disableGps = disableGps;}

		public boolean isDisableNetworkNasz() {return disableNetworkNasz;}

		public void setDisableNetworkNasz(boolean disableNetworkNasz) {this.disableNetworkNasz = disableNetworkNasz;}

		public boolean isDisableNetworkObcy() {return isDisableNetworkObcy;}

		public void setDisableNetworkObcy(boolean disableNetworkObcy) {isDisableNetworkObcy = disableNetworkObcy;}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public LocalizationServices getLocalizationServices(Location location){
		if(location instanceof LocationNetwork)
			return ( (LocationNetwork)location ).getLocalizationServices();
		return null;
	}

	public DialogUserLocationGoogleMapController getGoogleMapSingleUserDialogController() {
		return googleMapSingleUserDialogController;
	}

	public Location getLocationToDisplayDetails() {
		return locationToDisplayDetails;
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
		return selectUserForLastLocations;
	}

	public void setSelectUser(User selectUser) {
		this.selectUserForLastLocations = selectUser;
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

	public boolean isCheckAreaEvent() {
		return checkAreaEvent;
	}

	public void setCheckAreaEvent(boolean checkAreaEvent) {
		this.checkAreaEvent = checkAreaEvent;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public WifiInfoRepository getWifiInfoRepository() {
		return wifiInfoRepository;
	}

	public void setWifiInfoRepository(WifiInfoRepository wifiInfoRepository) {
		this.wifiInfoRepository = wifiInfoRepository;
	}

	public CellInfoMobileRepository getCellInfoMobileRepository() {
		return cellInfoMobileRepository;
	}

	public void setCellInfoMobileRepository(
			CellInfoMobileRepository cellInfoMobileRepository) {
		this.cellInfoMobileRepository = cellInfoMobileRepository;
	}

	public AreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public AreaEventNetworkRepository getAreaEventNetworkRepository() {
		return areaEventNetworkRepository;
	}

	public void setAreaEventNetworkRepository(
			AreaEventNetworkRepository areaEventNetworkRepository) {
		this.areaEventNetworkRepository = areaEventNetworkRepository;
	}

	public AreaEventGPSRepository getAreaEventGPSRepository() {
		return areaEventGPSRepository;
	}

	public void setAreaEventGPSRepository(
			AreaEventGPSRepository areaEventGPSRepository) {
		this.areaEventGPSRepository = areaEventGPSRepository;
	}

	public GoogleMapUserComponentService getGoogleMapUserComponentService() {
		return googleMapUserComponentService;
	}

	public void setGoogleMapUserComponentService(
			GoogleMapUserComponentService googleMapUserComponentService) {
		this.googleMapUserComponentService = googleMapUserComponentService;
	}

	public RestSessionManager getRestSessionManager() {
		return restSessionManager;
	}

	public void setRestSessionManager(RestSessionManager restSessionManager) {
		this.restSessionManager = restSessionManager;
	}

	public LokalizatorSession getLokalizatorSession() {
		return lokalizatorSession;
	}

	public void setLokalizatorSession(LokalizatorSession lokalizatorSession) {
		this.lokalizatorSession = lokalizatorSession;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Set<Long> getActiveAreaIds() {
		return activeAreaIds;
	}

	public void setActiveAreaIds(Set<Long> activeAreaIds) {
		this.activeAreaIds = activeAreaIds;
	}

	public void setGoogleMapController(UserGoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public void setGoogleMapSingleUserDialogController(
			DialogUserLocationGoogleMapController googleMapSingleUserDialogController) {
		this.googleMapSingleUserDialogController = googleMapSingleUserDialogController;
	}

	public void setGoogleMapStyle(String googleMapStyle) {
		this.googleMapStyle = googleMapStyle;
	}

	public void setLocationToDisplayDetails(Location locationToDisplayDetails) {
		this.locationToDisplayDetails = locationToDisplayDetails;
	}

	public void setGoogleMapVisible(GoogleMapComponentVisible googleMapVisible) {
		this.googleMapVisible = googleMapVisible;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public boolean isCreateRoutes() {
		return createRoutes;
	}

	public void setCreateRoutes(boolean createRoutes) {
		this.createRoutes = createRoutes;
	}

	public User getSelectUserForLastLocations() {
		return selectUserForLastLocations;
	}

	public void setSelectUserForLastLocations(User selectUserForLastLocations) {
		this.selectUserForLastLocations = selectUserForLastLocations;
	}

	public RouteManager getGpsRouteManager() {
		return gpsRouteManager;
	}

	public void setGpsRouteManager(RouteManager gpsRouteManager) {
		this.gpsRouteManager = gpsRouteManager;
	}

	public RouteManager getNetworkNaszRouteManager() {
		return networkNaszRouteManager;
	}

	public void setNetworkNaszRouteManager(RouteManager networkNaszRouteManager) {
		this.networkNaszRouteManager = networkNaszRouteManager;
	}

	public RouteManager getNetworkObcyRouteManager() {
		return networkObcyRouteManager;
	}

	public void setNetworkObcyRouteManager(RouteManager networkObcyRouteManager) {
		this.networkObcyRouteManager = networkObcyRouteManager;
	}

	public UserViewSettingDialog getUserViewSettingDialog() {
		return userViewSettingDialog;
	}

	public void setUserViewSettingDialog(UserViewSettingDialog userViewSettingDialog) {
		this.userViewSettingDialog = userViewSettingDialog;
	}

	public User getSelectUserForUserData() {
		return selectUserForUserData;
	}

	public void setSelectUserForUserData(User selectUserForUserData) {
		this.selectUserForUserData = selectUserForUserData;
	}


}
