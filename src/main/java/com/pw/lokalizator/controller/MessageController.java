package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.event.SelectEvent;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;

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
	
	
	private Area selectedArea;
	private List<Area> polygonList;
	private List<AreaEvent>areaEvents;
	
	@PostConstruct
	private void postConstruct(){
		long id = lokalizatorSession.getUser().getId();
		polygonList = areaRepository.findWithEagerFetchPointsAndTargetByProviderId(id); 
	}
	
	public void onPolygonSelect(SelectEvent event){
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
	
	private void sortAreaEventByDate(List<AreaEvent>areaEvents){
		areaEvents = areaEvents.stream()
		          .sorted((a1,a2) -> a1.getLocation().getDate().compareTo( a2.getLocation().getDate()))
		          .collect(Collectors.toList());
		          
	}
	
	public void onChooseLocationToDisplayInDialog(AreaEvent areaEvent){
		System.out.println(areaEvent);
	}
	
	public List<AreaEvent> getAreaEvents() {
		return areaEvents;
	}

	public Area getSelectedPolygonModel() {
		return selectedArea;
	}

	public void setSelectedPolygonModel(Area selectedPolygonModel) {
		this.selectedArea = selectedPolygonModel;
	}

	public List<Area> getPolygonList() {
		return polygonList;
	}
}
