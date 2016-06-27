package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.CircleBuilder;
import com.pw.lokalizator.jsf.utilitis.MarkerBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.LokalizatorSession;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.repository.AreaPointRepository;

@ViewScoped
@Named("messageController")
public class MessageController implements Serializable{
	@Inject
	private LokalizatorSession lokalizatorSession;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private AreaEventGPSRepository areaEventGPSRepository;
	@Inject
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Inject
	private AreaPointRepository areaPointRepository;
	
	private GoogleMapModel dialogMap;
	private String dialogMapCenter;
	private int dialogMapZoom;
	
	
	private Area selectedArea;
	private List<Area> areaList;
	private List<AreaEvent>areaEvents;
	
	@PostConstruct
	private void postConstruct(){
		long id = lokalizatorSession.getUser().getId();
		areaList = areaRepository.findWithEagerFetchPointsAndTargetByProviderId(id); 
		
		dialogMapZoom = 15;
	}
	
	public void onAreaSelect(SelectEvent event){
		selectedArea = (Area)event.getObject();
		areaEvents = findAreaAction(selectedArea);
		sortAreaEventByDate(areaEvents);
	}
	
	private List<AreaEvent> findAreaAction(Area area){
		List<AreaEvent>areaEvents = new ArrayList<AreaEvent>();
		
		long id = area.getId();
		areaEvents.addAll(areaEventGPSRepository.findByAreaId(id));
		areaEvents.addAll(areaEventNetworkRepository.findByAreaId(id));
		
		return areaEvents;
	}
	
	public String getLocalicationService(AreaEvent areaEvent){
		Location location = areaEvent.getLocation();
		
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			return locationNetwork.getLocalizationServices().toString();
		}
		
		return null;
	}
	
	private void sortAreaEventByDate(List<AreaEvent>areaEvents){
		areaEvents = areaEvents.stream()
		          .sorted((a1,a2) -> a1.getLocation().getDate().compareTo( a2.getLocation().getDate() ))
		          .collect(Collectors.toList());       
	}
	
	public void onDisplayLocationInDialog(AreaEvent areaEvent){
		dialogMap = new GoogleMapModel();
		dialogMap.addOverlay( CircleBuilder.createCircle( areaEvent.getLocation() ) );
		dialogMap.addOverlay( MarkerBuilder.createMarker( areaEvent.getLocation() ) );
		
		Location location = areaEvent.getLocation();
		dialogMapCenter = GoogleMapModel.center(location);
	}
	
	public void onDisplayAreaInDialog(Area area){
		List<AreaPoint>areaPoints = areaPointRepository.findByAreaId(area.getId());
		area.setPoints(getAreaPoint(areaPoints));
		
		Polygon polygon = PolygonBuilder.create(area);
		
		dialogMap =  new GoogleMapModel();
		dialogMap.addOverlay(polygon);
	}
	
	private Map<Integer, AreaPoint> getAreaPoint(List<AreaPoint>areaPoints){
		Map<Integer, AreaPoint>map = new HashMap<Integer, AreaPoint>();
		
		for(int i = 0; i < areaPoints.size(); i++){
			map.put(i, areaPoints.get(i));
		}
		
		return map;
	}
	
	public List<AreaEvent> getAreaEvents() {
		return areaEvents;
	}

	public Area getSelectedArea() {
		return selectedArea;
	}

	public void setSelectedArea(Area selectedPolygonModel) {
		this.selectedArea = selectedPolygonModel;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public GoogleMapModel getDialogMap() {
		return dialogMap;
	}

	public String getDialogMapCenter() {
		return dialogMapCenter;
	}

	public int getDialogMapZoom() {
		return dialogMapZoom;
	}

}
