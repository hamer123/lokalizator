package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.jboss.logging.Logger;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.LokalizatorSession;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaMessageMail;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.model.enums.AreaMailMessageModes;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.repository.AreaPointRepository;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="polyglon")
@ViewScoped
public class PolygonViewController implements Serializable{
	@Inject
	private LokalizatorSession lokalizatorSession;
	
	@Inject
	private UserRepository userRepository;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaPointRepository polygonPointRepository;
	
	@Inject
	private GoogleMapController googleMapController;
	@Inject 
	private Logger logger;
	
	private static final AreaMailMessageModes[] areaMailMessageModes = AreaMailMessageModes.values();
	private static final AreaFollows[] polygonFollowTypes = AreaFollows.values();
	
	private Polygon polygon;
	private Area area;
	private List<Area>areaList = new ArrayList<Area>();
	private List<String>targetLoginList;

	@PostConstruct
	private void postConstruct(){
		clearArea();
		polygon = PolygonBuilder.createEmpty();
		areaList = areaRepository.findByProviderId( lokalizatorSession.getUser().getId() );
		googleMapController.addOverlay(polygon);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////   ACTIONS   ///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onSaveArea(){
		try{
			
			if(isAreaWithName(area.getName())){
				JsfMessageBuilder.errorMessage("Obszar sledzenia o tej nazwie juz istnieje... zmien nazwe");
				return;
			} 
			
			if(polygon.getPaths().size() < 2){
				JsfMessageBuilder.errorMessage("Obszar sledzenia musi posiadac przynaimej 2 punkty");
				return;
			}

			prepareAreaBeforeSave();
			area = areaRepository.create(area);
			areaList.add(area);
			clearArea();
			JsfMessageBuilder.infoMessage("Udalo sie utworzyc obszar sledzenia o nazwie " + area.getName() + " sledzacy uzytkownika " + area.getTarget().getLogin());
		} catch(Exception e) {
			JsfMessageBuilder.errorMessage("Nie udalo sie utworzyc obszaru sledzenia " + e);
			e.printStackTrace();
		}
	}
	
	public void onChangeAreaActiveStatus(Area area){
		try{	
			if(area.isAktywny()){
				areaRepository.updateAktywnyById(false, area.getId());
				area.setAktywny(false);
				JsfMessageBuilder.infoMessage("Udalo sie dezaktywowac obszar sledzenia");
			} else {
				areaRepository.updateAktywnyById(true, area.getId());
				area.setAktywny(true);
				JsfMessageBuilder.infoMessage("Udalo sie aktywowac obszar sledzenia");
			}
		} catch(Exception e) {
			logger.error("[PolygonViewController] Nie udalo sie zmienic stanu aktywnosci obszaru sledzenia " + e);
			JsfMessageBuilder.errorMessage("Nie udalo sie zmienic stanu aktywnosci obszaru sledzenia");
		}
	}
	
	public void onRemoveArea(Area area){
		try{
			areaRepository.delete(area.getId());
			areaList.remove(area);
			JsfMessageBuilder.infoMessage("Udalo sie usunac polygon");
		} catch(Exception e) {
			logger.error("[PolygonViewController] Nie udalo sie usunac PolygonModel dla id: " + area.getId());
			JsfMessageBuilder.errorMessage("Nie udalo sie usunac polygon");
		}
	}
	
	public List<String> onAutoCompleteUser(String userLogin){
		return userRepository.findLoginByLoginLike(userLogin);
	}
	
	public void onShowArea(Area area){
		Area areaToDisplay  = copyArea(area);
		
		List<AreaPoint>points = polygonPointRepository.findByAreaId(area.getId());
		setArea(areaToDisplay);
		
		List<LatLng>paths =  convertAreaPoint(points);
		polygon.setPaths(paths);
		setPolygonFillColor(area.getColor());
	}
	
	public void onPointShow(LatLng latLng){
		googleMapController.setCenter( GoogleMapModel.center(latLng) );
	}
	
	public void onPathRemove(LatLng latLng, int index){
		removePathFromPolygon(latLng);
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        addPathToPolygon(latlng);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////   UTILITIES   ///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean isAreaWithName(String name){
		for(Area area : areaList)
			if(area.getName().equals(name))
				return true;
		
		return false;
	}
	
	private void prepareAreaBeforeSave(){
		User user = userRepository.findByLogin(area.getTarget().getLogin());
		area.setTarget(user);
		area.setProvider(lokalizatorSession.getUser());
		area.setPoints( convertLatLng(polygon.getPaths(), area) );
	}

	private void addPathToPolygon(LatLng latLng){
		polygon.getPaths().add(latLng);
	}
	
	private void removePathFromPolygon(LatLng latLng){
		List<LatLng>paths = polygon.getPaths();
		paths.remove(latLng);
	}
	
	private Map<Integer, AreaPoint> convertLatLng(List<LatLng>latLngs, Area area){
		Map<Integer, AreaPoint>map = new HashMap<Integer, AreaPoint>();
		
		for(int i = 0; i < latLngs.size(); i++){
			LatLng latLng = latLngs.get(i);
			
			AreaPoint areaPoint = new AreaPoint();
			areaPoint.setLat(latLng.getLat());
			areaPoint.setLng(latLng.getLng());
			areaPoint.setArea(area);
			areaPoint.setNumber(i);
			
			map.put(i, areaPoint);
		}
		
		return map;
	}

	private List<LatLng> convertAreaPoint(List<AreaPoint>points){
		List<LatLng>paths = new ArrayList<LatLng>();
		
		for(AreaPoint point : points){
			LatLng latLng = new LatLng(point.getLat(), point.getLng());
			paths.add(latLng);
		}
		
		return paths;
	}
	
	private Area copyArea(Area area){
		Area copy = new Area();
		
		copy.setName(area.getName());
		copy.setColor(area.getColor());
		copy.setAreaFollowType(area.getAreaFollowType());
		
		AreaMessageMail areaMessageMail = new AreaMessageMail();
		areaMessageMail.setActive(area.getAreaMessageMail().isActive());
		areaMessageMail.setAreaMailMessageMode(area.getAreaMessageMail().getAreaMailMessageMode());
		copy.setAreaMessageMail(areaMessageMail);
		
		copy.setTarget(area.getTarget());
		copy.setProvider(area.getProvider());
		
		return copy;
	}
	
	private void setPolygonFillColor(String color){
		if(color.startsWith("#"))
			polygon.setFillColor(color);
		else
			polygon.setFillColor("#" + color);
	}

	private void clearArea(){
		area = new Area();
		area.setName("");
		area.setAreaMessageMail(new AreaMessageMail());
		area.setTarget(new User());
		area.setPoints(new HashMap<Integer, AreaPoint>());
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////  GETTERS SETTERS  ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public List<LatLng> getPaths(){
		return polygon.getPaths();
	}

	public String getAreaActiveButtonValue(Area area){
		if(area.isAktywny())
			return "Dezaktywuj";
		
		return "Aktywuj";
	}
	
	public List<String> getListTargetsId() {
		return targetLoginList;
	}

	public void setListTargetsId(List<String> listTargetsId) {
		this.targetLoginList = listTargetsId;
	}

	public AreaFollows[] getPolygonFollowTypes() {
		return polygonFollowTypes;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public AreaMailMessageModes[] getAreaMailMessageModes() {
		return areaMailMessageModes;
	}


	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public GoogleMapController getGoogleMapController() {
		return googleMapController;
	}
}
