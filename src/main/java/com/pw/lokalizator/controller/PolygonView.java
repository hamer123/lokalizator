package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="polyglon")
@ViewScoped
public class PolygonView implements Serializable{
	@Inject
	private LokalizatorSession lokalizatorSession;
	@Inject
	private GoogleMapSetting googleMapSetting;
	@Inject
	private UserRepository userRepository;
	
	private MapModel polygonModel;
	private List<LatLng>points;
	private List<User>listFriends;
	private List<String>listTargetsId;
	
	@PostConstruct
	private void postConstruct(){
		polygonModel = new DefaultMapModel();
		points = new ArrayList<LatLng>();
		Polygon polygon = new Polygon();
		
        polygon.setStrokeColor("#FF9900");
        polygon.setFillColor("#FF9900");
        polygon.setStrokeOpacity(0.7);
        polygon.setFillOpacity(0.7);
        
        polygonModel.addOverlay(polygon);
        
        listFriends = userRepository.findFriendsById( lokalizatorSession.getCurrentUser().getId() );
	}
	
	public void onStateChanged(StateChangeEvent event){
		googleMapSetting.setCenter(event.getCenter());
		googleMapSetting.setZoom(event.getZoomLevel());
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        points.add(latlng);
        polygonModel.getPolygons().get(0).getPaths().add(latlng);
	}
	
	public void actionRemove(String index){
		points.remove( Integer.parseInt(index) - 1 );
		polygonModel.getPolygons().get(0).getPaths().remove( Integer.parseInt(index) - 1 );
	}

	public MapModel getPolygonModel() {
		return polygonModel;
	}

	public void setPolygonModel(MapModel polygonModel) {
		this.polygonModel = polygonModel;
	}

	public List<LatLng> getPoints() {
		return points;
	}

	public void setPoints(List<LatLng> points) {
		this.points = points;
	}

	public LokalizatorSession getLokalizatorSession() {
		return lokalizatorSession;
	}

	public void setLokalizatorSession(LokalizatorSession lokalizatorSession) {
		this.lokalizatorSession = lokalizatorSession;
	}

	public GoogleMapSetting getGoogleMapSetting() {
		return googleMapSetting;
	}

	public void setGoogleMapSetting(GoogleMapSetting googleMapSetting) {
		this.googleMapSetting = googleMapSetting;
	}

	public List<User> getListFriends() {
		return listFriends;
	}

	public void setListFriends(List<User> listFriends) {
		this.listFriends = listFriends;
	}

	public List<String> getListTargetsId() {
		return listTargetsId;
	}

	public void setListTargetsId(List<String> listTargetsId) {
		this.listTargetsId = listTargetsId;
	}

}
