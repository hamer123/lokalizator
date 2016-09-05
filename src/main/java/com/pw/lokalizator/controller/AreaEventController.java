package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.model.map.Polygon;
import com.pw.lokalizator.model.google.map.GoogleMapModel;
import com.pw.lokalizator.model.google.component.GoogleLocation;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaPointRepository;
import com.pw.lokalizator.serivce.qualifier.UserGoogleMap;
import com.pw.lokalizator.service.GoogleMapUserComponentService;

@Named
@ViewScoped
public class AreaEventController implements Serializable{
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaPointRepository areaPointRepository;
	@Inject
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Inject @UserGoogleMap
	private GoogleMapController googleMapController;
	
	@Param(name = "id") @Inject
	private long id;
	@Param(name = "provider") @Inject
	private Providers provider;
	
	private String msgError;
	private AreaEvent areaEvent;
	private boolean validParams;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS   //////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostConstruct
	public void postConstruct(){
		if(validParameters()){
			areaEvent = findEvent();
			if(validateAreaEvent()){
				Area area = fetchAreaPoints(areaEvent.getArea());
				areaEvent.setArea(area);
				
				Polygon polygon = googleMapUserComponentService.polygon(areaEvent.getArea());
				GoogleLocation googleLocation = googleMapUserComponentService.position(areaEvent.getLocation());
				
				googleMapController.addOverlay(polygon);
				googleMapController.addOverlay(googleLocation.overlays());
				
				googleMapController.setCenter(GoogleMapModel.center(areaEvent.getLocation()));
				validParams = true;
			}
		}
	}
	
	public void onPokazLokacje(){
		Location location = areaEvent.getLocation();
		googleMapController.setCenter(GoogleMapModel.center(location));
	}
	
	public void onPokazObszar(){
		AreaPoint point = areaEvent.getArea()
		                           .getPoints()
		                           .get(0);
		googleMapController.setCenter(GoogleMapModel.center(point.getLat(), point.getLng()));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////   UTILITIS   //////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Area fetchAreaPoints (Area area){
		Map<Integer,AreaPoint>points = areaPointRepository.findByAreaIdOrderByNumberMapToNumber(area.getId());
		area.setPoints(points);
		return area;
	}
	
	boolean validParameters(){
		if(id == 0){
			msgError = "Podany id nie jest poprawny";
			return false;
		}
		
		if(provider == null){
			msgError = "Podany provider nie jest poprawny";
			return false;
		}
		
		return true;
	}
	
	boolean validateAreaEvent(){
		if(areaEvent == null){
			msgError = "Nie znaleziono rekordu w bazie danych";
			return false;
		}
		return true;
	}
	
	AreaEvent findEvent(){
		if(provider == Providers.GPS)
			return areaEventGPSRepository.findById(id);
		else
			return areaEventNetworkRepository.findById(id);
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////    GETTERS SETTERS     /////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public AreaEventGPSRepository getAreaEventGPSRepository() {
		return areaEventGPSRepository;
	}
	public void setAreaEventGPSRepository(
			AreaEventGPSRepository areaEventGPSRepository) {
		this.areaEventGPSRepository = areaEventGPSRepository;
	}
	public AreaEventNetworkRepository getAreaEventNetworkRepository() {
		return areaEventNetworkRepository;
	}
	public void setAreaEventNetworkRepository(
			AreaEventNetworkRepository areaEventNetworkRepository) {
		this.areaEventNetworkRepository = areaEventNetworkRepository;
	}
	public Providers getProvider() {
		return provider;
	}
	public void setProvider(Providers provider) {
		this.provider = provider;
	}
	public AreaEvent getAreaEvent() {
		return areaEvent;

	}
	public void setAreaEvent(AreaEvent areaEvent) {
		this.areaEvent = areaEvent;
	}
	public boolean isValidParams() {
		return validParams;
	}
	public void setValidParams(boolean validParams) {
		this.validParams = validParams;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	public AreaPointRepository getAreaPointRepository() {
		return areaPointRepository;
	}
	public void setAreaPointRepository(AreaPointRepository areaPointRepository) {
		this.areaPointRepository = areaPointRepository;
	}
	public GoogleMapUserComponentService getGoogleMapUserComponentService() {
		return googleMapUserComponentService;
	}
	public void setGoogleMapUserComponentService(
			GoogleMapUserComponentService googleMapUserComponentService) {
		this.googleMapUserComponentService = googleMapUserComponentService;
	}

	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}

	public void setGoogleMapController(GoogleMapController googleMapController) {
		this.googleMapController = googleMapController;
	}
	
}
