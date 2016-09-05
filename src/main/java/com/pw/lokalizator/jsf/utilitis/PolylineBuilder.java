package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.List;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.service.LocationService;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;

public class PolylineBuilder {
	private static String GPS_POLYLINE_COLOR;
	private static String NETWORK_NASZ_POLYLINE_COLOR;
	private static String NETWORK_OBCY_POLYLINE_COLOR;
	private static double POLYLINE_STROKE_OPACITY;
	private static int POLYLINE_STROKE_WEIGHT;
	
	static{
		PropertiesReader propertiesReader = new PropertiesReader("lokalizator");
		findProperties(propertiesReader);
	}
	
	public static Polyline create(List<Location>locations){
		Polyline polyline = new Polyline();
		polyline.setId( id(locations.get(0)) );
		polyline.setData ( locations );
		polyline.setPaths( path(locations) );
		polyline.setStrokeColor( color(polyline, locations.get(0)) );
		polyline.setStrokeOpacity( POLYLINE_STROKE_OPACITY );
		polyline.setStrokeWeight( POLYLINE_STROKE_WEIGHT );
		return polyline;
	}

	public static Polyline create(Location location){
		Polyline polyline = new Polyline();
		List<Location>locations = new ArrayList<>();
		locations.add(location);
		polyline.setId(id(location));
		polyline.setData(location);
		polyline.setPaths(path(locations));
		polyline.setStrokeColor( color(polyline, location) );
		polyline.setStrokeOpacity( POLYLINE_STROKE_OPACITY );
		polyline.setStrokeWeight( POLYLINE_STROKE_WEIGHT );
		return polyline;
	}

	public static Polyline createNoData(Location location){
		Polyline polyline = new Polyline();
		List<Location>locations = new ArrayList<>();
		locations.add(location);
		polyline.setId(id(location));
		polyline.setPaths(path(locations));
		polyline.setStrokeColor( color(polyline, location) );
		polyline.setStrokeOpacity( POLYLINE_STROKE_OPACITY );
		polyline.setStrokeWeight( POLYLINE_STROKE_WEIGHT );
		return polyline;
	}

	public static Polyline createNoData(List<Location>locations){
		Polyline polyline = new Polyline();
		polyline.setId(id(locations.get(0)));
		polyline.setPaths(path(locations));
		polyline.setStrokeColor(color(polyline, locations.get(0)));
		polyline.setStrokeOpacity(POLYLINE_STROKE_OPACITY);
		polyline.setStrokeWeight(POLYLINE_STROKE_WEIGHT);
		return polyline;
	}

	public static Polyline empty(User user, Providers providers, LocalizationServices localizationServices){
		Polyline polyline = new Polyline();
		OverlayIdentyfikator overlayIdentyfikator = new OverlayIdentyfikatorBuilder().login(user.getLogin())
				                                    .localzationServiceType(localizationServices)
				                                    .overlayType(Overlays.POLYLINE)
				                                    .providerType(providers)
				                                    .build();
		polyline.setId(overlayIdentyfikator.createIdentyfikator());
		polyline.setPaths(new ArrayList<>());
		polyline.setStrokeColor(color(providers, localizationServices));
		polyline.setStrokeOpacity(POLYLINE_STROKE_OPACITY);
		polyline.setStrokeWeight(POLYLINE_STROKE_WEIGHT);
		return polyline;
	}

	private static String color(Providers providers, LocalizationServices localizationServices){
		if(providers == Providers.GPS)
			return GPS_POLYLINE_COLOR;
		else if(providers == Providers.NETWORK){
			if(localizationServices.equals(LocalizationServices.NASZ))
				return NETWORK_NASZ_POLYLINE_COLOR;
			else
				return NETWORK_OBCY_POLYLINE_COLOR;
		} else {
			throw new RuntimeException("Nie oblusgiwany Provider " + providers);
		}
	}

	private static String id(Location location){
		return new OverlayIdentyfikator(location, Overlays.POLYLINE).createIdentyfikator();
	}
	
	private static List<LatLng> path(List<Location>locations){
		List<LatLng>paths = new ArrayList<LatLng>();
		
		for(Location location : locations)
			paths.add(new LatLng(location.getLatitude(), location.getLongitude()));
		
		return paths;
	}
	
	private static String color(Polyline polyline, Location location){
		if(location instanceof LocationGPS){
			return GPS_POLYLINE_COLOR;
		} else {
			LocationNetwork locationNetwork = (LocationNetwork)location;
			
			if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
				return NETWORK_NASZ_POLYLINE_COLOR;
			else
				return NETWORK_OBCY_POLYLINE_COLOR;
		}
	}
	
	private static void findProperties(PropertiesReader propertiesReader){
		GPS_POLYLINE_COLOR = propertiesReader.findPropertyByName("GPS_POLYLINE_COLOR");
		NETWORK_NASZ_POLYLINE_COLOR = propertiesReader.findPropertyByName("NETWORK_NASZ_POLYLINE_COLOR");
		NETWORK_OBCY_POLYLINE_COLOR = propertiesReader.findPropertyByName("NETWORK_OBCY_POLYLINE_COLOR");
		POLYLINE_STROKE_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYLINE_STROKE_OPACITY") );
		POLYLINE_STROKE_WEIGHT = Integer.valueOf( propertiesReader.findPropertyByName("POLYLINE_STROKE_WEIGHT") );
	}
}
