package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;

public class CircleBuilder {
	private static String GPS_CIRCLE_COLOR;
	private static String NETWORK_NASZ_CIRCLE_COLOR;
	private static String NETWORK_OBCY_CIRCLE_COLOR;
	private static String GPS_CIRCLE_STROKE_COLOR;
	private static String NETWORK_NASZ_CIRCLE_STROKE_COLOR;
	private static String NETWORK_OBCY_CIRCLE_STROKE_COLOR;
	private static double CIRCLE_STROKE_OPACITY;
	private static double CIRCLE_FILL_OPACITY;
	
	static{
		PropertiesReader propertiesReader = new PropertiesReader("lokalizator");
		findProperties(propertiesReader);
	}
	
	private CircleBuilder(){
		
	}

	public static List<Circle> createCircle(List<Location>locationList){
		List<Circle>circleList = new ArrayList<Circle>();
		
		for(Location location : locationList){
			circleList.add(createCircleInstance(location));
		}
		
		return circleList;
	}
	
	public static Circle createCircle(Location location){
		return createCircleInstance(location);
	}
	
	private static Circle createCircleInstance(Location location){
		Circle circle = new Circle(new LatLng(location.getLatitude(), location.getLongitude()),
                100);

       circle.setData(location);
       circle.setRadius(getRadius(location));
       circle.setFillColor(chooseColor(location));
       circle.setFillOpacity(CIRCLE_FILL_OPACITY);
       circle.setStrokeColor(chooseStrokeColor(location));
       circle.setStrokeOpacity(CIRCLE_STROKE_OPACITY);
       OverlayIdentyfikator identyfikator = new OverlayIdentyfikator(location, Overlays.CIRCLE);
       circle.setId(identyfikator.createIdentyfikator());
       
       return circle;
	}
	
	private static double getRadius(Location location){
		return location.getAccuracy() * 100;
	}
	
	private static String chooseColor(Location location){
		Providers type = location.getProviderType();
		
		if(type == Providers.GPS)
			return GPS_CIRCLE_COLOR;
		else if(type == Providers.NETWORK)
			return chooseColorNetwork(location);
		else
			throw new IllegalStateException("[CircleBuilder] Nie znaleziono koloru dla providera " + type);
	}
	
	private static String chooseColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		
		if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
			return NETWORK_NASZ_CIRCLE_COLOR;
		else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
			return NETWORK_OBCY_CIRCLE_COLOR;
		else
			throw new IllegalStateException("[CircleBuilder] Nie znaleziono koloru dla Network Localization Services " + locationNetwork.getLocalizationServices());
	}
	
	private static String chooseStrokeColor(Location location){
		Providers type = location.getProviderType();
		
		if(type == Providers.GPS){
			return GPS_CIRCLE_STROKE_COLOR;
		} else if(type == Providers.NETWORK){
			return chooseStrokeColorNetwork(location);
		} else {
			throw new IllegalStateException("[CircleBuilder] Nie znaleziono stroke koloru dla providera " + type);
		}
	}
	
	private static String chooseStrokeColorNetwork(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		
		if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
			return NETWORK_NASZ_CIRCLE_STROKE_COLOR;
		else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
			return NETWORK_OBCY_CIRCLE_STROKE_COLOR;
		else
			throw new IllegalStateException("[CircleBuilder] Nie znaleziono stroke koloru dla Network Localization Services " + locationNetwork.getLocalizationServices());
	}
	
	private static void findCircleColor(PropertiesReader propertiesReader){
		GPS_CIRCLE_COLOR = propertiesReader.findPropertyByName("GPS_CIRCLE_COLOR");
		NETWORK_NASZ_CIRCLE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_CIRCLE_COLOR");
		NETWORK_OBCY_CIRCLE_COLOR = propertiesReader.findPropertyByName("OWN_CIRCLE_COLOR");
	}
	
	private static void findCircleStrokeColor(PropertiesReader propertiesReader){
		GPS_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("GPS_CIRCLE_STROKE_COLOR");
		NETWORK_NASZ_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_CIRCLE_STROKE_COLOR");
		NETWORK_OBCY_CIRCLE_STROKE_COLOR = propertiesReader.findPropertyByName("NETWORK_OBCY_CIRCLE_STROKE_COLOR");
	}
	
	private static void findCircleStrokeOpacity(PropertiesReader propertiesReader){
		CIRCLE_STROKE_OPACITY = Double.parseDouble( propertiesReader.findPropertyByName("CIRCLE_STROKE_OPACITY") );
		CIRCLE_FILL_OPACITY = Double.parseDouble( propertiesReader.findPropertyByName("CIRCLE_FILL_OPACITY") );
	}
	
	private static void findProperties(PropertiesReader propertiesReader){
		findCircleColor(propertiesReader);
		findCircleStrokeColor(propertiesReader);
		findCircleStrokeOpacity(propertiesReader);
	}
}

