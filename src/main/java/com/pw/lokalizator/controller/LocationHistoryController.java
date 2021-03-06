package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.model.google.map.GoogleMapModel;
import com.pw.lokalizator.model.google.component.Route;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.LocationGPSRepository;
import com.pw.lokalizator.repository.LocationNetworkRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.serivce.qualifier.UserGoogleMap;
import com.pw.lokalizator.service.GoogleMapUserComponentService;
import com.pw.lokalizator.service.RouteService;

@Named("locationHistory")
@ViewScoped
public class LocationHistoryController implements Serializable{
	@Inject
	private LocationNetworkRepository locationNetworkRepository;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private UserRepository userRepository;
	@Inject @UserGoogleMap
	private GoogleMapController googleMapController;
	@Inject
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject
	private RouteService routeService;
	@Inject
	private Logger logger;
	
	private int maxRekords = 1000;
	private Date older;
	private Date younger;
	private String login;
	private Providers provider;
	private LocalizationServices localizationServices;
	
	private Route route;
	private Location location;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS    /////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onShowRoute(){
		try{
			List<Location>locations = findLocations();
			if(locations.size() < 2){
				notEnoughLocation();
				return;
			}
			route = googleMapUserComponentService.route(locations);
			googleMapController.replace(route.overlays());
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki");
			logger.error(e);
		}
	}
	
	public void onLocationSelect(){
		 googleMapController.setCenter( GoogleMapModel.center(location) );
	}
	
	public List<String> onAutoCompleteLogin(String login){
		return userRepository.findLoginByLoginLike(login);
	}
	
	public void onCalculateRouteLenght(){
		double lenghtDouble = routeService.calculateLenghtMeters(route);
		String lenght = new DecimalFormat("#.##").format(lenghtDouble);
		JsfMessageBuilder.infoMessage(lenght + " meters");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   UTILITIS    ////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private List<Location> findLocations(){
		List<Location>locations = new ArrayList<Location>();
		
		if(provider == Providers.GPS){
			locations.addAll( findLocationGPS() );
		} else {
			locations.addAll( findLocationNetwork() );
		} 
		
		return locations;
	}
	
	private List<LocationNetwork> findLocationNetwork(){
		if(localizationServices == LocalizationServices.NASZ){
			return locationNetworkRepository.
					findByLoginAndDateForServiceNaszOrderByDate(login, younger, older, maxRekords);
		} else {
			return locationNetworkRepository.
					findByLoginAndDateForServiceObcyOrderByDate(login, younger, older, maxRekords);
		}
	}
	
	private List<LocationGPS> findLocationGPS(){
		return locationGPSRepository.
				findByLoginAndDateOrderByDate(login, younger, older, maxRekords);
	}
	
	private void notEnoughLocation(){
		JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki, za malo lokacji");
		googleMapController.clear();
		route = null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   GETTERS SETTERS    /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public List<Location> getLocations() {
		if(route != null)
			return (List<Location>) route.getPolyline().getData();
		
		return new ArrayList<>();
	}
	
	public Route getRoute() {
		return route;
	}

	public Providers[] providers(){
		return Providers.values();
	}
	
	public LocalizationServices[] localizationServices(){
		return LocalizationServices.values();
	}
	
	public int getMaxRekords() {
		return maxRekords;
	}

	public void setMaxRekords(int maxRekords) {
		this.maxRekords = maxRekords;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public String getLogin() {
		return login;
	}
	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public Date getOlder() {
		return older;
	}

	public void setOlder(Date older) {
		this.older = older;
	}

	public Date getYounger() {
		return younger;
	}

	public void setYounger(Date younger) {
		this.younger = younger;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	public Providers getProvider() {
		return provider;
	}
	public void setProvider(Providers provider) {
		this.provider = provider;
	}
	public LocalizationServices getLocalizationServices() {
		return localizationServices;
	}

	public void setLocalizationServices(LocalizationServices localizationServices) {
		this.localizationServices = localizationServices;
	}

	public LocationNetworkRepository getLocationNetworkRepository() {
		return locationNetworkRepository;
	}

	public void setLocationNetworkRepository(
			LocationNetworkRepository locationNetworkRepository) {
		this.locationNetworkRepository = locationNetworkRepository;
	}

	public LocationGPSRepository getLocationGPSRepository() {
		return locationGPSRepository;
	}

	public void setLocationGPSRepository(LocationGPSRepository locationGPSRepository) {
		this.locationGPSRepository = locationGPSRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public GoogleMapUserComponentService getGoogleMapUserComponentService() {
		return googleMapUserComponentService;
	}

	public void setGoogleMapUserComponentService(
			GoogleMapUserComponentService googleMapUserComponentService) {
		this.googleMapUserComponentService = googleMapUserComponentService;
	}

	public RouteService getRouteService() {
		return routeService;
	}

	public void setRouteService(RouteService routeService) {
		this.routeService = routeService;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
