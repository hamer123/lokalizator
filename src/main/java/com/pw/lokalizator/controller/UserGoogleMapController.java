package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.LokalizatorSession;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.UserSetting;
import com.pw.lokalizator.model.enums.GoogleMaps;
import com.pw.lokalizator.serivce.qualifier.UserGoogleMap;

@Named
@Dependent
@UserGoogleMap
public class UserGoogleMapController extends GoogleMapController{
	private static final long serialVersionUID = 2183283451821061250L;
	@Inject
	private LokalizatorSession lokalizatorSession;

	@PostConstruct
	public void postConstruct(){
		googleMapModel = new GoogleMapModel();
		UserSetting userSetting = lokalizatorSession.getUser().getUserSetting();
		zoom = userSetting.getgMapZoom();
		center = GoogleMapModel.center(userSetting.getDefaultLatitude(), userSetting.getDefaultLongtitude());
		googleMapType = GoogleMaps.HYBRID;
		streetVisible = true;
	}
	
	public LokalizatorSession getLokalizatorSession() {
		return lokalizatorSession;
	}

	public void setLokalizatorSession(LokalizatorSession lokalizatorSession) {
		this.lokalizatorSession = lokalizatorSession;
	}
}
