package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

import com.pw.lokalizator.jsf.utilitis.CircleBuilder;
import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.jsf.utilitis.MarkerBuilder;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.GoogleMapModel.GoogleMapModelBuilder;
import com.pw.lokalizator.model.OverlayVisibility;
import com.pw.lokalizator.model.OverlayVisibility.OverlayVisibilityBuilder;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.PolygonModel;
import com.pw.lokalizator.model.entity.PolygonPoint;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.GoogleMaps;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.PolygonModelRepository;
import com.pw.lokalizator.repository.UserRepository;

@ViewScoped
@Named("googleMapControllerFollowMode")
public class GoogleMapControllerFollowMode implements Serializable{
	private static Logger LOG = Logger.getLogger(GoogleMapControllerFollowMode.class);
	@EJB
	private UserRepository userRepository;
	@EJB
	private PolygonModelRepository polygonModelRepository;
	@EJB
	private LocationRepository locationRepository;
	
	private MapModel googleMapModel;
	private OverlayVisibility gpsVisibility;
	private OverlayVisibility networkObcaUslugaVisibilty;
	private OverlayVisibility networkNaszaUslugaVisibility;
	private boolean polygonVisible;
	private Set<User>followUsers;
	private String center;
	private int zoom;
	

	@PostConstruct
	private void postConstruct(){
		googleMapModel = new GoogleMapModel();
		gpsVisibility = new OverlayVisibility();
		networkObcaUslugaVisibilty = new OverlayVisibility();
		networkNaszaUslugaVisibility = new OverlayVisibility();
		polygonVisible = true;	
		followUsers = new HashSet<User>();
		center = "51.6014053, 18.9724216";
		zoom = 10;
	}
	
	public void onGoogleMapStateChange(StateChangeEvent event){
		center = createCenter(event.getCenter().getLat(), 
				              event.getCenter().getLng());
		
		zoom = event.getZoomLevel();
	}
	
	public String createCenter(double lat, double lon){
		return    lat 
				+ ", "
				+ lon;
	}
	
	public void update(){
		updateFollowUsers();
		googleMapModel = createGoogleMap();
	}
	
	public void render(){
		googleMapModel = createGoogleMap();
	}
	
	public MapModel getGoogleMapModel(){
		return googleMapModel;
	}
	
	public User getUser(String login){
		for(User user : followUsers){
			if(user.getLogin().equals(login))
				return user;
		}
		throw new NotFoundException("Nie znaleziono na liscie do sledzenia uzytkownika " + login);
	}
	
	public void addUser(String login){
		User user = userRepository.findUserWithPolygonsByLogin(login);
		followUsers.add(user);
		List<Location>locations = getCurrentLocationFromUser(user);
		googleMapModel.getCircles().addAll( createCircle(locations) );
		googleMapModel.getMarkers().addAll( createMarker(locations) );
		googleMapModel.getPolygons().addAll( createPolygon(user.getPolygons()) );
	}
	
	public void removeUser(String login){
		removeUserFromFollow(login);
		removeUserFromGoogleMap(login);
	}
	
	public void removeUserFromGoogleMap(String login){
		OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder()
		                                           .login(login)
		                                           .build();
		
		Pattern pattern = identyfikator.createPattern();
		
		removeMarkersUsingOverlayIdPattern(pattern);
		removeCirclesUsingOverlayIdPattern(pattern);
	}
	
	private void removeMarkersUsingOverlayIdPattern(Pattern pattern){
		Iterator<Marker>iterator = googleMapModel.getMarkers().iterator();
		while(iterator.hasNext()){
			Marker marker = iterator.next();
			if(pattern.matcher(marker.getId()).matches())
				iterator.remove();
		}
	}
	
	private void removeCirclesUsingOverlayIdPattern(Pattern pattern){
		Iterator<Circle>iterator = googleMapModel.getCircles().iterator();
		while(iterator.hasNext()){
			Circle circle = iterator.next();
			if(pattern.matcher(circle.getId()).matches())
				iterator.remove();
		}
	}
	
	
	public void removeUserFromFollow(String login){
		Iterator<User>iterator = followUsers.iterator();
		
		while(iterator.hasNext()){
			User user = iterator.next();
			if(user.getLogin().equals(login)){
				iterator.remove();
				return;
			}
		}
	}
	
	private void updateFollowUsers(){
		updateFollowUsersCurrentLocations();
	}
	
	private void updateFollowUsersCurrentLocations(){
		List<User>usersList = new ArrayList<User>();
		
		for(User user : followUsers){
		   User userUpdate = userRepository.findById(user.getId());
		   usersList.add( userUpdate );
		}
		
		for(User user : usersList){
			User userToUpdate = getUser(user.getLogin());
			userToUpdate.setLastLocationGPS(user.getLastLocationGPS());
			userToUpdate.setLastLocationNetworkNaszaUsluga(user.getLastLocationNetworkNaszaUsluga());
			userToUpdate.setLastLocationNetworObcaUsluga(user.getLastLocationNetworObcaUsluga());
		}
	}

	private MapModel createGoogleMap(){
		List<Location>locations = getCurrentLocationsFromUsers();
		List<PolygonModel>polygonsModel = getPolygonModelFromUsers();
		
		List<Circle>circles = createCircle(locations);
		List<Marker>markers = createMarker(locations);
		List<Polygon>polygons = createPolygon(polygonsModel);

		return new GoogleMapModelBuilder()
		              .circles(circles)
				      .markers(markers)
				      .polygon(polygons)
				      .build();
	}

	private List<Location>getCurrentLocationsFromUsers(){
		List<Location>locations = new ArrayList<>();
		
		for(User user : followUsers){
			if(user.getLastLocationGPS() != null)
				locations.add(user.getLastLocationGPS());
			if(user.getLastLocationNetworkNaszaUsluga() != null)
				locations.add(user.getLastLocationNetworkNaszaUsluga());
			if(user.getLastLocationNetworObcaUsluga() != null)
			    locations.add(user.getLastLocationNetworObcaUsluga());
		}
		
		return locations;
	}
	
	private List<Location>getCurrentLocationFromUser(User user){
		List<Location>location = new ArrayList<Location>();
		if(user.getLastLocationGPS() != null)
			location.add(user.getLastLocationGPS());
		if(user.getLastLocationNetworkNaszaUsluga() != null)
			location.add(user.getLastLocationNetworkNaszaUsluga());
		if(user.getLastLocationNetworObcaUsluga() != null)
		    location.add(user.getLastLocationNetworObcaUsluga());
		
		return location;
	}
	
	private List<PolygonModel> getPolygonModelFromUsers(){
		List<PolygonModel>polygons = new ArrayList<>();
		
		for(User user : followUsers){
			polygons.addAll(user.getPolygons());
		}
		
		return polygons;
	}
	
	private List<Circle> createCircle(List<Location>locations){
		List<Circle>circles = new ArrayList<>();

		for(Location location : locations){
			if(shouldCreateOverlayForProvider(location, Overlays.CIRCLE)){
				circles.add(CircleBuilder.createCircle(location));
			}
		}
		
		return circles;
	}
	
	private List<Marker> createMarker(List<Location>locations){
		List<Marker>markers = new ArrayList<>();
		
		for(Location location : locations){
			if( shouldCreateOverlayForProvider(location, Overlays.MARKER) ){
				markers.add( MarkerBuilder.createMarker(location) );
			}
		}
		
		return markers;
	}
	
	private List<Polygon> createPolygon(List<PolygonModel>polygonsModel){
		List<Polygon>polygons = new ArrayList<>();
		
		for(PolygonModel polygonModel : polygonsModel){
			if(polygonVisible){
				polygons.add( PolygonBuilder.create(polygonModel) );
			}
		}
		
		return polygons;
	}

	
	private boolean shouldCreateOverlayForProvider(Location location, Overlays overlay){
		Providers provider = location.getProviderType();
		
		switch(provider){
		case GPS:
			return shouldCreateOverlay(gpsVisibility, overlay);
		case NETWORK:
			return shouldCreateOverlay(getOverlayVisibilityForNetworkProvider(location), overlay);
		default:
			throw new IllegalArgumentException("[GoogleMapController] Ten provider nie jest wspierany do ustawien widocznosci " + provider);
		}
	}
	
	private OverlayVisibility getOverlayVisibilityForNetworkProvider(Location location){
		LocationNetwork locationNetwork = (LocationNetwork) location;
		
		if(locationNetwork.getLocalizationServices() == LocalizationServices.NASZ)
			return networkNaszaUslugaVisibility;
		else if(locationNetwork.getLocalizationServices() == LocalizationServices.OBCY)
			return networkObcaUslugaVisibilty;
		else
		   throw new IllegalArgumentException("[GoogleMapController] Nie ma OverlayVisibility dla tego rodzaju uslugi lokalizacji " + locationNetwork.getLocalizationServices());
	}
	
	private boolean shouldCreateOverlay(OverlayVisibility overlayVisibility, Overlays overlay){
		switch(overlay){
		case CIRCLE:
			return overlayVisibility.isCircleVisible();
		case MARKER:
			return overlayVisibility.isMarkerVisible();
		case POLYGON:
			throw new NotSupportedException("[GoogleMapController] Ten overlay nie jest dzielony miedzy providerow " + overlay);
		case POLYLINE:
			return overlayVisibility.isPolylineVisible();
		case RECTANGLE:
			return overlayVisibility.isRectangleVisibel();
		default:
			throw new NotSupportedException("[GoogleMapController] Ten overlay nie jest wykorzystywany do ustawien widocznosci " + overlay);
		}
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public Set<User> getUsersToFollow() {
		return followUsers;
	}

	public OverlayVisibility getGpsVisibility() {
		return gpsVisibility;
	}

	public OverlayVisibility getNetworkObcaUslugaVisibilty() {
		return networkObcaUslugaVisibilty;
	}

	public OverlayVisibility getNetworkNaszaUslugaVisibility() {
		return networkNaszaUslugaVisibility;
	}
	
}
