package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.utilitis.PropertiesReader;

public class MarkerBuilder {
	private static Logger LOG = Logger.getLogger(MarkerBuilder.class);
	
	private static String GPS_MARKER_ICON_URL;
	private static String NETWORK_MARKER_OBCA_USLUGA_ICON_URL;
	private static String NETWORK_MARKER_NASZA_USLUGA_ICON_URL;
	
	static{
		PropertiesReader propertiesReader = new PropertiesReader("lokalizator");
		findProperties(propertiesReader);
	}
	
	private MarkerBuilder(){
		
	}
	
	private static Marker createMarkerInstance(Location location){
		Marker marker = new Marker(new LatLng(location.getLatitude(), location.getLongitude()));
		marker.setIcon(chooseIcon(location));
		marker.setData(location);
		marker.setDraggable(false);
		marker.setClickable(true);
		marker.setTitle(createTitle(location));
		OverlayIdentyfikator identyfikator = new OverlayIdentyfikator(location);
		marker.setId(identyfikator.createIdentyfikator());
		
		return marker;
	}
	
	private static String createTitle(Location location){
		String title = location.getProviderType()
				     + " "
				     + location.getUser().getLogin()
				     + " "
				     + location .getDate();
		return title;
	}

	private static String chooseIcon(Location location){
		switch(location.getProviderType()){
		case GPS:
			return GPS_MARKER_ICON_URL;
		case NETWORK:
			LocationNetwork locationNetwork = (LocationNetwork)location;
			if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
				return NETWORK_MARKER_NASZA_USLUGA_ICON_URL;
			else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
				return NETWORK_MARKER_OBCA_USLUGA_ICON_URL;
		default:
			throw new IllegalArgumentException("Nie ma dla takiego providera icony");
		}
	}
	
	public static List<Marker> createMarker(List<Location>locationList){
		List<Marker>markerList = new ArrayList<Marker>();
		
		for(Location location : locationList)
			markerList.add(createMarkerInstance(location));
		
		return markerList;
	}
	
	public static Marker createMarker(Location location){
		return createMarkerInstance(location);
	}
	
	private static void findProperties(PropertiesReader propertiesReader){
		findMarkerIconUrl(propertiesReader);
	}
	
	private static void findMarkerIconUrl(PropertiesReader propertiesReader){
		GPS_MARKER_ICON_URL = propertiesReader.findPropertyByName("GPS_MARKER_ICON_URL");
		NETWORK_MARKER_NASZA_USLUGA_ICON_URL = propertiesReader.findPropertyByName("NETWORK_MARKER_NASZA_USLUGA_ICON_URL");
		NETWORK_MARKER_OBCA_USLUGA_ICON_URL  = propertiesReader.findPropertyByName("NETWORK_MARKER_OBCA_USLUGA_ICON_URL");
	}
}

