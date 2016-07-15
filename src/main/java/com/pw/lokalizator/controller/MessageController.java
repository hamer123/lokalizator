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
	@Inject
	private GoogleMapController dialogMap;
	
	private Area selectedArea;
	private List<Area> areaList;
	private List<AreaEvent>areaEvents;
	
	@PostConstruct
	private void postConstruct(){
		long id = lokalizatorSession.getUser().getId();
		areaList = areaRepository.findWithEagerFetchPointsAndTargetByProviderId(id); 
		dialogMap.setZoom(15);
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
		dialogMap.clear();
		Location location = areaEvent.getLocation();
		dialogMap.addOverlay(CircleBuilder.createCircle(location));
		dialogMap.addOverlay(MarkerBuilder.createMarker(location));
		dialogMap.setCenter(GoogleMapModel.center((location)));
	}
	
	public void onDisplayAreaInDialog(Area area){
		List<AreaPoint>areaPoints = areaPointRepository.findByAreaId(area.getId());
		dialogMap.clear();
		area.setPoints(mapAreaPoint(areaPoints));
		Polygon polygon = PolygonBuilder.create(area);
		dialogMap.addOverlay(polygon);
		AreaPoint areaPoint = areaPoints.get(0);
		dialogMap.setCenter(GoogleMapModel.center(areaPoint.getLat(), areaPoint.getLng()));
	}
	
	private Map<Integer, AreaPoint> mapAreaPoint(List<AreaPoint>areaPoints){
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

	public LokalizatorSession getLokalizatorSession() {
		return lokalizatorSession;
	}

	public void setLokalizatorSession(LokalizatorSession lokalizatorSession) {
		this.lokalizatorSession = lokalizatorSession;
	}

	public AreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
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

	public AreaPointRepository getAreaPointRepository() {
		return areaPointRepository;
	}

	public void setAreaPointRepository(AreaPointRepository areaPointRepository) {
		this.areaPointRepository = areaPointRepository;
	}

	public GoogleMapController getDialogMap() {
		return dialogMap;
	}

	public void setDialogMap(GoogleMapController dialogMap) {
		this.dialogMap = dialogMap;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	public void setAreaEvents(List<AreaEvent> areaEvents) {
		this.areaEvents = areaEvents;
	}

}
