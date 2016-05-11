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
import com.pw.lokalizator.jsf.utilitis.PolygonBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.GoogleMapModel.GoogleMapModelBuilder;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.OverlayVisibility;
import com.pw.lokalizator.model.OverlayVisibility.OverlayVisibilityBuilder;
import com.pw.lokalizator.model.GoogleMapTypes;
import com.pw.lokalizator.model.Overlays;
import com.pw.lokalizator.model.PolygonModel;
import com.pw.lokalizator.model.PolygonPoint;
import com.pw.lokalizator.model.Providers;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.PolygonModelRepository;
import com.pw.lokalizator.repository.UserRepository;

@ViewScoped
@Named("googleMapController")
public class GoogleMapController implements Serializable{
	private static Logger LOG = Logger.getLogger(GoogleMapController.class);
	@EJB
	private UserRepository userRepository;
	@EJB
	private PolygonModelRepository polygonModelRepository;
	@EJB
	private LocationRepository locationRepository;
	
	private MapModel googleMapModel;
	private OverlayVisibility gpsVisibility;
	private OverlayVisibility networkVisibilty;
	private OverlayVisibility ownVisibility;
	private Set<User>followUsers;
	private List<User>newUsers;
	
	private boolean isNewSingleUser;
	private User singleUser;
	private Date locationFromDate;
	private Date locationToDate;
	
	private GoogleMapControllerModes mode;
	
	private boolean isPolygonVisibility; 
	private GoogleMapTypes googleMapType;
	private boolean streetView;
	private String center;
	private int zoom;
	

	@PostConstruct
	private void init(){
		googleMapModel = new DefaultMapModel();
		mode = GoogleMapControllerModes.FOLLOW_USERS;
		
		gpsVisibility = new OverlayVisibility();
		networkVisibilty = new OverlayVisibility();
		ownVisibility = new OverlayVisibility();
		
		followUsers = new HashSet<User>();
		newUsers = new ArrayList<User>();	
		
		isPolygonVisibility = true;
		
		googleMapType = GoogleMapTypes.HYBRID;
		streetView = true;
		center = "51.6014053, 18.9724216";
		zoom = 10;

		googleMapModel = new DefaultMapModel();
		
//		//TEST
//		User user = new User();
//		user.setId(1L);
//		user.setLogin("hamer123");
//		followUsers.add(user);

	}
	
	public void onGoogleMapStateChange(StateChangeEvent event){
		center = event.getCenter().getLat() 
			   + ", " 
			   + event.getCenter().getLng();
		
		zoom = event.getZoomLevel();
	}
	
	public void update(){
		switch(mode){
		case FOLLOW_USERS:
			updateGoogleMapFollowUserMode();
			break;
		case SINGLE_USER:
			updateGoogleMapSingleUserMode();
			break;
		}
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
	
	public void addUser(User user){
		newUsers.add(user);
	}
	
	public void setSingleUser(User user){
		singleUser = user;
		isNewSingleUser = isNewUser(user.getLogin());
	}
	
	public void removeUser(String login){
		Iterator<User>iterator = followUsers.iterator();
		
		while(iterator.hasNext()){
			User user = iterator.next();
			if(user.getLogin().equals(login)){
				iterator.remove();
				return;
			}
		}
	}
	
	private boolean isNewUser(String login){
		return login.equals(singleUser.getLogin());
	}
	
	private void updateGoogleMapFollowUserMode(){
		if(isNewUsers()){
			followUsers.addAll(newUsers);
			updateNewFollowUsersPolygons();
			newUsers.clear();
		}
		
		updateFollowUsers();
		googleMapModel = createGoogleMapFollowUsers();
	}
	
	private void updateGoogleMapSingleUserMode(){
		if(isNewSingleUser){
			singleUser.setPolygons(findUserPolygonsModel(singleUser.getId()));
			isNewSingleUser = false;
		}
		
		updateSingleUser();
		googleMapModel = createGoogleMapSingleUser();
	}
	
	private Set<Long>getUsersId(Collection<User>users){
		Set<Long>usersIdList = new HashSet<Long>();
		
		for(User user : users){
			usersIdList.add(user.getId());
		}
		
		return usersIdList;
	}
	
	private void updateFollowUsers(){
		updateFollowUsersCurrentLocations();
	}
	
	private void updateSingleUser(){
		updateSingleUserLocations();
	}
	
	private void updateSingleUserLocations(){
		List<Location>locations = locationRepository.findByUserIdWhereYoungerThanAndOlderThanOrderByDateDesc(
				                                        singleUser.getId(),
				                                        locationFromDate,
				                                        locationToDate);
		
		singleUser.setLocations(locations);
	}
	
	private void updateFollowUsersCurrentLocations(){
		Set<Long>usersIdList = getUsersId(followUsers);
		List<User>usersList = new ArrayList<User>();
		
		for(User user : followUsers){
		   usersList.add( userRepository.findById(user.getId()) );
		   //userRepository.findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(user.getId()) 
		}
		
		for(User user : usersList){
			User userToUpdate = getUser(user.getLogin());
			userToUpdate.setLastGpsLocation(user.getLastGpsLocation());
			userToUpdate.setLastNetworkLocation(user.getLastNetworkLocation());
			userToUpdate.setLastOwnProviderLocation(user.getLastOwnProviderLocation());
		}
	}
	
	private void updateNewFollowUsersPolygons(){
		for(User user : newUsers){
			user.setPolygons( findUserPolygonsModel(user.getId()) );
		}
	}
	
	private List<PolygonModel>findUserPolygonsModel(Long id){
		return polygonModelRepository.getPolygonsByTargetId(id);
	}
	
	private boolean isNewUsers(){
		return newUsers.size() > 0;
	}
	
	private MapModel createGoogleMapFollowUsers(){
		List<Location>locations = getCurrentLocationsFromUsers();
		List<PolygonModel>polygonsModel = getPolygonModelFromUsers();
		
		List<Circle>circles = createCircles(locations);
		List<Marker>markers = createMarkers(locations);
		List<Polygon>polygons = createPolygons(polygonsModel);
		
		
		MapModel model = new DefaultMapModel();
		

		return new GoogleMapModelBuilder()
		              .circles(circles)
				      .markers(markers)
				      .polygon(polygons)
				      .build();
	}
	
	private GoogleMapModel createGoogleMapSingleUser(){
		List<Location>locations = singleUser.getLocations();
		
		List<Circle>circles = createCircles(locations);
		List<Marker>markers = createMarkers(locations);
		List<Polyline>polylines = createPolylines(locations); //TODO
		
		return new GoogleMapModelBuilder()
		              .circles(circles)
		              .markers(markers)
		              .polylines(polylines)
		              .build();
	}
	

	
	private List<Location>getCurrentLocationsFromUsers(){
		List<Location>locations = new ArrayList<>();
		
		for(User user : followUsers){
			locations.add(user.getLastGpsLocation());
			locations.add(user.getLastNetworkLocation());
			locations.add(user.getLastOwnProviderLocation());
		}
		
		return locations;
	}
	
	private List<PolygonModel> getPolygonModelFromUsers(){
		List<PolygonModel>polygons = new ArrayList<>();
		
		for(User user : followUsers){
			polygons.addAll(user.getPolygons());
		}
		
		return polygons;
	}
	
	
	private List<Circle> createCircles(List<Location>locations){
		List<Circle>circles = new ArrayList<>();

		for(Location location : locations){
			if(shouldCreateOverlayForProvider(location.getProvider(), Overlays.CIRCLE)){
				circles.add(CircleBuilder.createCircle(location));
			}
		}
		
		return circles;
	}
	
	private List<Marker> createMarkers(List<Location>locations){
		List<Marker>markers = new ArrayList<>();
		
		for(Location location : locations){
			if( shouldCreateOverlayForProvider(location.getProvider(), Overlays.MARKER) ){
				markers.add( MarkerBuilder.createMarker(location) );
			}
		}
		
		return markers;
	}
	
	private List<Polygon> createPolygons(List<PolygonModel>polygonsModel){
		List<Polygon>polygons = new ArrayList<>();
		
		for(PolygonModel polygonModel : polygonsModel){
			if(isPolygonVisibility){
				polygons.add( PolygonBuilder.create(polygonModel) );
			}
		}
		
		return polygons;
	}
	
	private List<Polyline>createPolylines(List<Location>locations){
		List<Polyline>polylines = new ArrayList<>();
		
		//TODO
		
		return null;
	}
	
	private boolean shouldCreateOverlayForProvider(Providers provider, Overlays overlay){
		switch(provider){
		case GPS:
			return shouldCreateOverlay(gpsVisibility, overlay);
		case NETWORK:
			return shouldCreateOverlay(networkVisibilty, overlay);
		case OWN:
			return shouldCreateOverlay(ownVisibility, overlay);
		default:
			throw new NotSupportedException("Ten provider nie jest wspierany " + provider);
		}
	}
	
	private boolean shouldCreateOverlay(OverlayVisibility overlayVisibility, Overlays overlay){
		switch(overlay){
		case CIRCLE:
			return overlayVisibility.isCircleVisible();
		case MARKER:
			return overlayVisibility.isMarkerVisible();
		case POLYGON:
			return isPolygonVisibility;
		case POLYLINE:
			return overlayVisibility.isPolylineVisible();
		case RECTANGLE:
			return overlayVisibility.isRectangleVisibel();
		default:
			throw new NotSupportedException("Ten overlay nie jest wspierany " + overlay);
		}
	}
	
	public static enum GoogleMapControllerModes{
		FOLLOW_USERS,SINGLE_USER;
	}

	public GoogleMapControllerModes getMode() {
		return mode;
	}

	public void setMode(GoogleMapControllerModes mode) {
		this.mode = mode;
	}

	public GoogleMapTypes getGoogleMapType() {
		return googleMapType;
	}

	public void setGoogleMapType(GoogleMapTypes googleMapType) {
		this.googleMapType = googleMapType;
	}

	public boolean isStreetView() {
		return streetView;
	}

	public void setStreetView(boolean streetView) {
		this.streetView = streetView;
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

	public void setUsersToFollow(Set<User> usersToFollow) {
		this.followUsers = usersToFollow;
	}
	
}
