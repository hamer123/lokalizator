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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.model.entity.PolygonModel;
import com.pw.lokalizator.model.entity.PolygonPoint;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.PolygonFollows;
import com.pw.lokalizator.repository.PolygonModelRepository;
import com.pw.lokalizator.repository.PolygonPointRepository;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="polyglon")
@ViewScoped
public class PolygonViewController implements Serializable{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LokalizatorSession lokalizatorSession;
	@Inject
	private PolygonModelRepository polygonModelRepository;
	@Inject
	private PolygonPointRepository polygonPointRepository;
	
	private Logger logger = Logger.getLogger(PolygonViewController.class);
	
	private MapModel googleMapModel;
	private Polygon polygon;
	private List<LatLng>polygonLatLngs;
	private List<String>listTargetsId;
	private List<PolygonModel>polygonList;
	
	private int zoom;
	private String center;
	
	private String nazwaPolygona;
	private PolygonFollows polygonFollowType;
	private String targetLogin;
	
	private static final PolygonFollows[] polygonFollowTypes = PolygonFollows.values();
	
	@PostConstruct
	private void postConstruct(){
		googleMapModel = new DefaultMapModel();
		polygonLatLngs = new ArrayList<LatLng>();
		polygon = PolygonBuilder.createEmpty();
		polygonList = new ArrayList<PolygonModel>();
		polygonList.addAll(findPolygonModels());
		googleMapModel.addOverlay(polygon);
		center = "51.6014053, 18.9724216";
		zoom = 15;
	}
	
	public void onRemovePolygon(long id){
		try{
			polygonModelRepository.remove(id);
			JsfMessageBuilder.infoMessage("Udalo sie usunac polygon");
		}catch(Exception e){
			logger.error("[PolygonViewController] Nie udalo sie usunac PolygonModel dla id: " + id + e);
			JsfMessageBuilder.errorMessage("Nie udalo sie usunac polygon");
		}
	}
	
	private List<PolygonModel>findPolygonModels(){
		long id = lokalizatorSession.getUser().getId();
		return polygonModelRepository.findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId(id); //findPolygonAndPointsAndTargetByProviderId(id); //
	}
	
	public List<String> onAutoCompleteUser(String userLogin){
		return userRepository.findLoginByLoginLike(userLogin);
	}
	
	public void createPolygon(){
		try{
			PolygonModel polygonModel = createPolygonModel();
			polygonModel = polygonModelRepository.create(polygonModel);
			polygonList.add(polygonModel);
			
			JsfMessageBuilder.infoMessage("Udalo sie utworzyc polygon o nazwie " + nazwaPolygona + " sledzacy uzytkownika " + targetLogin);
		}catch(Exception e){
			//TODO
			JsfMessageBuilder.errorMessage("Nie udalo sie utworzyc polygona");
		}
	}
	
	public PolygonModel createPolygonModel(){
		PolygonModel polygonModel = new PolygonModel();
		
		polygonModel.setName(nazwaPolygona);
		polygonModel.setPoints( createPolygonPoints(polygonLatLngs, polygonModel) );
		polygonModel.setPolygonFollowType(polygonFollowType);
		polygonModel.setProvider(lokalizatorSession.getUser());
		
		User target = userRepository.findByLogin(targetLogin);
		polygonModel.setTarget(target);
		
		return polygonModel;
	}
	
	private Map<Integer, PolygonPoint>createPolygonPoints(List<LatLng>latLngs, PolygonModel polygonModel){
		Map<Integer, PolygonPoint>points = new HashMap<Integer, PolygonPoint>();
		
		for(int i = 0; i<latLngs.size(); i++){
			LatLng latLng = latLngs.get(i);
			
			PolygonPoint point = new PolygonPoint();
			point.setLat(latLng.getLat());
			point.setLng(latLng.getLng());
			point.setNumber(i);
			point.setPolygon(polygonModel);
			
			points.put(i, point);
		}
		
		return points;
	}
	
	public void onPokazPolygon(PolygonModel polygonModel){
		List<PolygonPoint>points = polygonPointRepository.findByPolygonModelId(polygonModel.getId());
		List<LatLng>paths = getPathsFromPolygonPoints(points);
		polygon.setPaths(paths);
		nazwaPolygona = polygonModel.getName();
		polygonFollowType = polygonModel.getPolygonFollowType();
		targetLogin = polygonModel.getTarget().getLogin();
	}
	

	private List<LatLng> getPathsFromPolygonPoints(List<PolygonPoint>points){
		List<LatLng>paths = new ArrayList<LatLng>();
		
		for(PolygonPoint point : points){
			LatLng latLng = new LatLng(point.getLat(), point.getLng());
			paths.add(latLng);
		}
		
		return paths;
	}
	
	public void onStateChanged(StateChangeEvent event){
		center = createCenter(event.getCenter().getLat(), event.getCenter().getLng());
		zoom = event.getZoomLevel();
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        polygonLatLngs.add(latlng);
        addPathToPolygon(latlng);
	}
	
	private void addPathToPolygon(LatLng latLng){
		List<LatLng>paths = polygon.getPaths();
		paths.add(latLng);
	}
	
	public List<LatLng> getPaths(){
		return polygon.getPaths();
	}
	
	public void removePath(LatLng latLng){
		getPaths().remove(latLng);
	}
	
	public void showPath(LatLng latLng){
		center = createCenter(latLng.getLat(), latLng.getLng());
	}
	
	public void actionRemove(String index){
		polygonLatLngs.remove( Integer.parseInt(index) - 1 );
		googleMapModel.getPolygons().get(0).getPaths().remove( Integer.parseInt(index) - 1 );
	}
	
	public String createCenter(double lat, double lon){
		return    lat 
				+ ", "
				+ lon;
	}

	public MapModel getPolygonModel() {
		return googleMapModel;
	}

	public void setPolygonModel(MapModel polygonModel) {
		this.googleMapModel = polygonModel;
	}

	public List<LatLng> getPoints() {
		return polygonLatLngs;
	}

	public void setPoints(List<LatLng> points) {
		this.polygonLatLngs = points;
	}

	public List<String> getListTargetsId() {
		return listTargetsId;
	}

	public void setListTargetsId(List<String> listTargetsId) {
		this.listTargetsId = listTargetsId;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getNazwaPolygona() {
		return nazwaPolygona;
	}

	public void setNazwaPolygona(String nazwaPolygona) {
		this.nazwaPolygona = nazwaPolygona;
	}

	public PolygonFollows getPolygonType() {
		return polygonFollowType;
	}

	public void setPolygonType(PolygonFollows polygonType) {
		this.polygonFollowType = polygonType;
	}

	public String getTargetLogin() {
		return targetLogin;
	}

	public void setTargetLogin(String targetLogin) {
		this.targetLogin = targetLogin;
	}

	public PolygonFollows[] getPolygonFollowTypes() {
		return polygonFollowTypes;
	}

	public List<PolygonModel> getPolygonList() {
		return polygonList;
	}

	public void setPolygonList(List<PolygonModel> polygonList) {
		this.polygonList = polygonList;
	}

}
