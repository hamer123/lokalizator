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

import com.pw.lokalizator.model.PolygonModel;
import com.pw.lokalizator.model.PolygonPoint;
import com.pw.lokalizator.utilitis.PropertiesReader;

public class PolygonBuilder {
	private static Logger LOG = Logger.getLogger(CircleBuilder.class);
	
	private static String POLYGON_STROKE_COLOR;
	private static String POLYGON_FILL_COLOR;
	private static int POLYGON_FILL_OPACITY;
	private static int POLYGON_STROKE_OPACITY;
	
	static{
		PropertiesReader propertiesReader = new PropertiesReader("lokalizator");
		findProperties(propertiesReader);
	}
	
	public static Polygon create(PolygonModel polygonModel){
		return createPolygonInstance(polygonModel);
	}
	
	public static List<Polygon> createPolygon(List<PolygonModel>polygonModelList){
		List<Polygon>polygonList = new ArrayList<Polygon>();
		
		for(PolygonModel polygonModel : polygonModelList)
			polygonList.add(createPolygonInstance(polygonModel));
		
		return polygonList;
	}
	
	private static Polygon createPolygonInstance(PolygonModel polygonModel){
		Polygon polygon = new Polygon();
		polygon.setFillColor(POLYGON_FILL_COLOR);
		polygon.setFillOpacity(POLYGON_FILL_OPACITY);
		polygon.setStrokeColor(POLYGON_STROKE_COLOR);
		polygon.setStrokeOpacity(POLYGON_STROKE_OPACITY);
		polygon.setData(polygonModel);
		
		List<LatLng>pathList = createPaths(polygonModel.getPoints());
		polygon.setPaths(pathList);
		
		return polygon;
	}
	
	private static List<LatLng> createPaths(Map<Integer, PolygonPoint> polygonPointsMap){
		List<LatLng>latlngList = new ArrayList<>();
		
		List<Integer>numberList = getSortedPointNumber( polygonPointsMap.keySet() );
		for(int number : numberList){
			PolygonPoint polygonPoint = polygonPointsMap.get(number);
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
		POLYGON_FILL_OPACITY = Integer.valueOf( propertiesReader.findPropertyByName("POLYGON_FILL_OPACITY") );
		POLYGON_STROKE_OPACITY = Integer.valueOf( propertiesReader.findPropertyByName("POLYGON_STROKE_OPACITY") );
	}
}
