package com.pw.lokalizator.model.google.component;

import java.util.ArrayList;
import java.util.List;

import com.pw.lokalizator.jsf.utilitis.PolylineBuilder;
import com.pw.lokalizator.model.google.map.GoogleMapComponent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polyline;
import com.pw.lokalizator.model.entity.Location;

public class Route implements GoogleMapComponent {
	private Marker start;
	private Marker end;
	private Polyline polyline;
	private boolean visible = true;
	
	@Override
	/** zwraca wszystkie komponenty w postaci listy overlayow */
	public List<Overlay> overlays() {
		List<Overlay>overlays = new ArrayList<Overlay>();
		if(start != null) overlays.add(start);
		if(end != null) overlays.add(end);
		if(polyline != null) overlays.add(polyline);
		return overlays;
	}

	/** Kiedy nie chcemy Markerow oznaczajacyh poczatek i koniec */
	public static Route onlyPolyline(Polyline polyline) { return new Route(polyline); }
	/** Tworzy kazdy komponenet Markerk startu i konca wraz z sciezka */
	public static Route full(Marker start, Marker end, Polyline polyline) { return new Route(start, end, polyline);}

	private Route(Marker start, Marker end, Polyline polyline){
		this.start = start;
		this.end = end;
		this.polyline = polyline;
	}

	private Route(Polyline polyline){
		this.polyline = polyline;
	}

	/**
	 * Jesli byl Route stworzony jako empty tworzymy po prostu nowy polyline,
	 * jesli nie byl po prostu dodajemy nowa lokacje do sciezki
	 * */
	public boolean addPath(Location location){
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		return polyline.getPaths().add(latLng);
	}

	public boolean addDataToPolyline(Location location){
		return getLocations().add(location);
	}
	
	@SuppressWarnings("unchecked")
	/** Zwraca liste lokacji ktore tworza dana sciezke */
	public List<Location>getLocations(){
		return (List<Location>) polyline.getData();
	}
	
	public Polyline getPolyline(){
		return polyline;
	}
	
	public Location getLastLocation(){
		if(getLocations() != null)
			return getLocations().get(getLocations().size() - 1);
		return null;
	}

	public LatLng getLastPath(){
		int index = polyline.getPaths().size() - 1;
		return polyline.getPaths().get(index);
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setColor(String color){
		polyline.setStrokeColor(color);
	}

}
