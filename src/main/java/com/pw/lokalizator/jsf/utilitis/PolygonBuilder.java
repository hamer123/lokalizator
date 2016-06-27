package com.pw.lokalizator.jsf.utilitis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.utilitis.PropertiesReader;

public class PolygonBuilder {
	private static String POLYGON_STROKE_COLOR;
	private static String POLYGON_FILL_COLOR;
	private static double POLYGON_FILL_OPACITY;
	private static double POLYGON_STROKE_OPACITY;
	
	static{
		PropertiesReader propertiesReader = new PropertiesReader("lokalizator");
		findProperties(propertiesReader);
	}
	
	public static Polygon create(Area polygonModel){
		return createPolygonInstance(polygonModel);
	}
	
	public static Polygon createEmpty(){
		Polygon polygon = new Polygon();
		setupPolygonView(polygon);
		return polygon;
	}
	
	public static List<Polygon> createPolygons(List<Area>polygonModelList){
		List<Polygon>polygonList = new ArrayList<Polygon>();
		
		for(Area polygonModel : polygonModelList)
			polygonList.add(createPolygonInstance(polygonModel));
		
		return polygonList;
	}
	
	private static void setupPolygonView(Polygon polygon){
		polygon.setFillColor(POLYGON_FILL_COLOR);
		polygon.setFillOpacity(POLYGON_FILL_OPACITY);
		polygon.setStrokeColor(POLYGON_STROKE_COLOR);
		polygon.setStrokeOpacity(POLYGON_STROKE_OPACITY);
	}
	
	private static void setupPolygonView(Polygon polygon, Area area){
		setPolzgonFillColor(polygon, area.getColor());
		polygon.setFillOpacity(POLYGON_FILL_OPACITY);
		polygon.setStrokeColor(POLYGON_STROKE_COLOR);
		polygon.setStrokeOpacity(POLYGON_STROKE_OPACITY);
	}
	
	private static void setPolzgonFillColor(Polygon polygon, String color){
		if(color.startsWith("#"))
			polygon.setFillColor(color);
		else
			polygon.setFillColor("#" + color);
	}
	
	private static Polygon createPolygonInstance(Area area){
		Polygon polygon = new Polygon();
		setupPolygonView(polygon, area);
		polygon.setData(area);
		
		OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().login(area.getProvider().getLogin())
				                                                              .id(area.getId())
				                                                              .overlayType(Overlays.POLYGON)
				                                                              .build();
		polygon.setId(identyfikator.createIdentyfikator());
		
		List<LatLng>pathList = createPaths(area.getPoints());
		polygon.setPaths(pathList);
		
		return polygon;
	}
	
	private static List<LatLng> createPaths(Map<Integer, AreaPoint> polygonPointsMap){
		List<LatLng>latlngList = new ArrayList<>();
		
		List<Integer>numberList = getSortedPointNumber( polygonPointsMap.keySet() );
		for(int number : numberList){
			AreaPoint polygonPoint = polygonPointsMap.get(number);
			LatLng latLng = new LatLng(polygonPoint.getLat(), polygonPoint.getLng());
			latlngList.add(latLng);
		}
		
		return latlngList;
	}
	
	private static List<Integer> getSortedPointNumber(Collection<Integer>numberCollection){
		List<Integer>pointSortedList = new ArrayList<Integer>(numberCollection);
		Collections.sort(pointSortedList);
		return pointSortedList;
	}
	
	private PolygonBuilder(){
		
	}
	
	private static void findProperties(PropertiesReader propertiesReader){
		findFillColorAndOpacity(propertiesReader);
		findStrokeColorAndOpacity(propertiesReader);
	}
	
	private static void findFillColorAndOpacity(PropertiesReader propertiesReader){
		POLYGON_FILL_COLOR = propertiesReader.findPropertyByName("POLYGON_FILL_COLOR");
		POLYGON_STROKE_COLOR = propertiesReader.findPropertyByName("POLYGON_STROKE_COLOR");
	}
	
	private static void findStrokeColorAndOpacity(PropertiesReader propertiesReader){
		POLYGON_FILL_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYGON_FILL_OPACITY") );
		POLYGON_STROKE_OPACITY = Double.valueOf( propertiesReader.findPropertyByName("POLYGON_STROKE_OPACITY") );
	}
}
