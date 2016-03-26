package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

@Named(value="polyglon")
@ViewScoped
public class PolygonView implements Serializable{
	@Inject
	private LokalizatorSession lokalizatorSession;
	
	private MapModel polygonModel;
	private Map<Integer, LatLng>points;
	private Polygon polygon;
	
	@PostConstruct
	private void postConstruct(){
		polygonModel = new DefaultMapModel();
		points = new HashMap<Integer, LatLng>();
		polygon = new Polygon();
		
        polygon.setStrokeColor("#FF9900");
        polygon.setFillColor("#FF9900");
        polygon.setStrokeOpacity(0.7);
        polygon.setFillOpacity(0.7);
        
        polygonModel.addOverlay(polygon);
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        points.put(points.size() + 1, latlng);
        polygon.getPaths().add(latlng);
        
        FacesContext.getCurrentInstance().addMessage(null, (new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng())));
	}

	public MapModel getPolygonModel() {
		return polygonModel;
	}

	public void setPolygonModel(MapModel polygonModel) {
		this.polygonModel = polygonModel;
	}

	public Map<Integer, LatLng> getPoints() {
		return points;
	}

	public void setPoints(Map<Integer, LatLng> points) {
		this.points = points;
	}
	
}
