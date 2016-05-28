package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.pw.lokalizator.model.entity.PolygonModel;
import com.pw.lokalizator.repository.PolygonModelRepository;

@ViewScoped
@Named("messageController")
public class MessageController implements Serializable{
	@Inject
	private LokalizatorSession lokalizatorSession;
	@Inject
	private PolygonModelRepository polygonModelRepository;
	
	private PolygonModel selectedPolygonModel;
	private List<PolygonModel> polygonList;
	
	@PostConstruct
	private void postConstruct(){
		long id = lokalizatorSession.getUser().getId();
		polygonList = polygonModelRepository.findWithEagerFetchPointsAndTargetByProviderId(id); 
	}
	
	public void onPolygonSelect(SelectEvent event){
		selectedPolygonModel = (PolygonModel)event.getObject();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public PolygonModel getSelectedPolygonModel() {
		return selectedPolygonModel;
	}

	public void setSelectedPolygonModel(PolygonModel selectedPolygonModel) {
		this.selectedPolygonModel = selectedPolygonModel;
	}

	public List<PolygonModel> getPolygonList() {
		return polygonList;
	}
}
