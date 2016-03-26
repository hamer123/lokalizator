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
	private List<LatLng>points;
	
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
	}
	
	public void onPointSelect(PointSelectEvent event){
        LatLng latlng = event.getLatLng();
        points.add(latlng);
        polygonModel.getPolygons().get(0).getPaths().add(latlng);

        FacesContext.getCurrentInstance().addMessage(null, (new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng())));
	}
	
	public void actionRemove(String index){
		System.err.println("REMOVE " + index);
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
	
}
