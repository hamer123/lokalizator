package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.model.map.Overlay;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.model.LokalizatorSession;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.LocationGPSRepository;
import com.pw.lokalizator.repository.LocationNetworkRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.GoogleMapUserComponentService;
import com.pw.lokalizator.service.LocationService;

@Named("locationHistory")
@ViewScoped
public class LocationHistoryController implements Serializable{
	@Inject
	private LocationNetworkRepository locationNetworkRepository;
	@Inject
	private LocationGPSRepository locationGPSRepository;
	@Inject
	private UserRepository userRepository;
	@Inject
	private GoogleMapController googleMapController;
	@Inject
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject
	private Logger logger;
	
	private Date older;
	private Date younger;
	private String login;
	private Providers provider;
	private LocalizationServices localizationServices; 

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS    /////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onShowRoute(){
		try{
			List<Location>locations = findLocations();
			
			if(locations.size() < 2){
				JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki, za malo lokacji");
				return;
			}
			
			List<Overlay>overlays = googleMapUserComponentService.route(locations);
			googleMapController.addOverlay(overlays);	
			
		} catch(Exception e){
			JsfMessageBuilder.errorMessage("Nie mozna utworzyc sciezki");
			logger.error(e);
		}
	}
	
	public List<String> onAutoCompleteLogin(String login){
		return userRepository.findLoginByLoginLike(login);
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
			return locationNetworkRepository.findByUserLoginAndDateAndServiceEqualsNaszOrderByDateDesc(login, younger, older);
		} else {
			return locationNetworkRepository.findByUserLoginAndDateAndServiceEqualsObcyOrderByDateDesc(login, younger, older);
		}
	}
	
	private List<LocationGPS> findLocationGPS(){
		return locationGPSRepository.findByUserLoginAndDateOrderByDateDesc(login, younger, older);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   GETTERS SETTERS    /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Providers[] providers(){
		return Providers.values();
	}
	
	public LocalizationServices[] localizationServices(){
		return LocalizationServices.values();
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
}
