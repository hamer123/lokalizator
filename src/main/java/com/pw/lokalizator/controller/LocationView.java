package com.pw.lokalizator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.jboss.resteasy.logging.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.model.FriendInvitation;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.ProviderType;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.FriendInvitationRepository;
import com.pw.lokalizator.repository.LocationRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.service.FriendService;

@Named(value="location")
@ViewScoped
public class LocationView implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private LokalizatorSession session;
	@Inject
	private GoogleMapSetting setting;
	@EJB
	private LocationRepository locationRepository;
	@EJB
	private FriendInvitationRepository friendInvitationRepository;
	@EJB
	private FriendService friendService;
	@EJB
	private UserRepository userRepository;
	
	private static final String BLUE_MARKER = "http://maps.google.com/mapfiles/ms/icons/blue-dot.png";
	private static final String RED_MARKER = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
	private static final String GREEN_MARKER = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
	private static final String YELLOW_MARKER = "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png";
	
	Logger log = Logger.getLogger(LocationView.class);
	
	private MapModel map;
    private Date fromDate;
    private Date endDate;
    private Date maxDate;
    private String friendNick;
    private Location selectedLocation;
    
    private boolean gpsVisible;
    private boolean networkVisible;
    private boolean ownVisible;
    
    private Marker oldLocationMarker;
    private List<Location>locations;
    //Klucz to id usera
    private Map<Long, Marker>gpsMarkers;
    private Map<Long, Marker>networkMarkers;
    private Map<Long, Marker>ownMarkers;
    private Map<Long, User>users;
    private List<FriendInvitation>friendInvitations;
    
	@PostConstruct
	private void postConstruct(){
		User user = userRepository.findById( session.getCurrentUser().getId() );
		gpsMarkers = new HashMap<Long, Marker>();
		networkMarkers = new HashMap<Long, Marker>();
		ownMarkers = new HashMap<Long, Marker>();
		
		gpsVisible = true;
		networkVisible = true;
		ownVisible = true;
		
		friendInvitations = friendInvitationRepository.getByOdbiorcaId( user.getId() );
		
		List<User>fList = userRepository.findFriendsById(user.getId());
		users = new HashMap<Long, User>();
		for(User u : fList){
			users.put(u.getId(), u);
			
			if(u.getLastGpsLocation() != null)
				gpsMarkers.put(u.getId(), createMarker(u.getLastGpsLocation(), u.getLogin()));
			
			if(u.getLastNetworkLocation() != null)
			    networkMarkers.put(u.getId(), createMarker(u.getLastNetworkLocation(), u.getLogin()));
			
			if(u.getLastOwnProviderLocation() != null)
			    ownMarkers.put(u.getId(), createMarker(u.getLastOwnProviderLocation(), u.getLogin()));
		}
		
		if(user.getLastGpsLocation() != null)
			gpsMarkers.put(user.getId(), createMarker(user.getLastGpsLocation(), user.getLogin()));
		
		if(user.getLastNetworkLocation() != null)
		    networkMarkers.put(user.getId(), createMarker(user.getLastNetworkLocation(), user.getLogin()));
		
		if(user.getLastOwnProviderLocation() != null)
		    ownMarkers.put(user.getId(), createMarker(user.getLastOwnProviderLocation(), user.getLogin()));
		
		//TODO
		maxDate = new Date();
		
		map = new DefaultMapModel();
		for(Marker m : gpsMarkers.values())
			map.addOverlay(m);
		for(Marker m : networkMarkers.values())
			map.addOverlay(m);
		for(Marker m : ownMarkers.values())
			map.addOverlay(m);
		
		log.info("size of marker " + map.getMarkers().size());
	}
	
	/*
	 * Find your location in pass
	 */
	public void findLocations(ActionEvent actionEvent){
		try{
			if(fromDate.getTime() > endDate.getTime()){
		        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bledne ustawienie przedialu daty",  null);
		        FacesContext.getCurrentInstance().addMessage(null, message);
		        return;
			}
			
			locations = locationRepository.getLocationYoungThanAndOlderThan(fromDate, endDate);
			
			if(locations.size() < 1){
		        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not find locations",  null);
		        FacesContext.getCurrentInstance().addMessage(null, message);
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Set changes in Objects
	 */
	public void onStateChanged(StateChangeEvent event){
		setting.setCenter(event.getCenter());
		setting.setZoom(event.getZoomLevel());
	}
	
	/*
	 * Update current location of yours and your friends
	 */
	public void onPoll(){
		log.info("poll is about to start for " + session.getUserLogin());
		
		try{
			Map<Long, Location>locations = null;
			
			//update GPS locations
			if(!gpsMarkers.isEmpty()){
				locations = locationRepository.getGpsByUserId( gpsMarkers.keySet() );
				for( Entry<Long,Location>entry : locations.entrySet() ){
					Location location = entry.getValue();
					Marker marker = gpsMarkers.get(entry.getKey());
					marker.setLatlng( new LatLng( location.getLatitude(), location.getLongitude()) );
					marker.setData( "GPS " + " [ " + users.get(entry.getKey()) + " ] DATE [ " +  location.getDate() + " ]");
				}
			}

			
			//update NETWORK locations
			if(!networkMarkers.isEmpty()){
				locations = locationRepository.getNetworkByUserId( networkMarkers.keySet() );
				for( Entry<Long,Location>entry : locations.entrySet() ){
					Location location = entry.getValue();
					Marker marker = networkMarkers.get(entry.getKey());
					marker.setLatlng( new LatLng( location.getLatitude(), location.getLongitude()) );
					marker.setData( "NETWORK " + " [ " + users.get(entry.getKey()) + " ] DATE [ " +  location.getDate() + " ]");
				}
			}
			
			//update OWN locations
			if(!ownMarkers.isEmpty()){
				locations = locationRepository.getOwnByUserId( ownMarkers.keySet() );
				for( Entry<Long,Location>entry : locations.entrySet() ){
					Location location = entry.getValue();
					Marker marker = ownMarkers.get(entry.getKey());
					marker.setLatlng( new LatLng( location.getLatitude(), location.getLongitude()) );
					marker.setData( "OWN " + " [ " + users.get(entry.getKey()) + " ] DATE [ " +  location.getDate() + " ]");
				}
			}

		}catch(Exception e){
			log.error("Exception podczas pollingu", e);
		}
	}
	
	/*
	 * Send invitation to user
	 */
	public void onSendInvitation(ActionEvent event){
		try{
			//check if u are arleady friends
			for(User u : users.values()){
				if(u.getLogin().equalsIgnoreCase(friendNick)){
			        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, friendNick + " jest juz na twoiej liscie przyjaciol",  null);
			        FacesContext.getCurrentInstance().addMessage(null, message);
			        return;
				}
			}
			
			//do siebie ?
			if(friendNick.equals( session.getUserLogin() )){
		        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie mozna wyslac zaproszenia do siebie",  null);
		        FacesContext.getCurrentInstance().addMessage(null, message);
		        return;
			}
			// go ahead !
			friendService.sendInvitation(session.getCurrentUser(), friendNick);
			// send message
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Zaproszenie zostalo wyslane do" + friendNick,  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        //try to handle exceptions
		}catch(Throwable ejbException){
				try {
					throw ejbException.getCause();
				}catch(IllegalArgumentException iae){
			        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Zaproszenie juz istnieje",  null);
			        FacesContext.getCurrentInstance().addMessage(null, message);
			    }catch(NoResultException lnre){
			        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "nie znaleziono uzytkownika o nicku " + friendNick,  null);
			        FacesContext.getCurrentInstance().addMessage(null, message);
				}catch(Throwable e){
			        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie udalo sie wyslac zaproszenia",  null);
			        FacesContext.getCurrentInstance().addMessage(null, message);
				}
		}
	}
	
	/*
	 * Display location in pass
	 */
	public void onRowSelect(SelectEvent event){
		Location location = (Location)event.getObject();
		
		if(oldLocationMarker == null){
			oldLocationMarker = new Marker(new LatLng( location.getLatitude(), location.getLongitude() ), location.getProvider() + " [ " + session.getUserLogin() +  " ] DATE [ "+ location.getDate() + " ]",
					                       null, YELLOW_MARKER);
			setting.setCenter(new LatLng( location.getLatitude(), location.getLongitude() ));
			map.addOverlay(oldLocationMarker);
		}else{
			oldLocationMarker.setLatlng(new LatLng( location.getLatitude(), location.getLongitude() ));
			setting.setCenter(new LatLng( location.getLatitude(), location.getLongitude() ));
		}
	}
	
	/*
	 * Display friend location
	 */
	public void onRowSelectFriend(SelectEvent event){
	//	Friend f = (Friend)event.getObject();
	//	setting.setCenter(new LatLng(f.getFriend().getLatitude(), f.getFriend().getLongitude()));
	}
	
	/*
	 * Select marker on the map
	 */
    public void onMarkerSelect(OverlaySelectEvent event) {
    	if(event.getOverlay() instanceof Marker){
            Marker marker = (Marker) event.getOverlay();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Selected", marker.getTitle()));
    	}
    }
    
    /*
     * Create marker
     */
    private Marker createMarker(Location location, String login){
    	if(location.getProvider() == ProviderType.GPS){
    		return  new Marker(
        			    new LatLng(location.getLatitude(), location.getLongitude()),
        			    location.getProvider() + " [ " + login + " ] DATE [ " + location.getDate() + " ]" ,
        			    null,
        			    BLUE_MARKER
        			    );
    	}else if(location.getProvider() == ProviderType.NETWORK){
    		return  new Marker(
    			        new LatLng(location.getLatitude(), location.getLongitude()),
    			        location.getProvider() + " [ " + login + " ] DATE [ " + location.getDate() + " ]" ,
    			        null,
    			        GREEN_MARKER
    			        );
    	}else{
    		return  new Marker(
			            new LatLng(location.getLatitude(), location.getLongitude()),
			            location.getProvider() + " [ " + login + " ] DATE [ " + location.getDate() + " ]" ,
			            null,
			            RED_MARKER
			            );
    	}
    }
    
    public void onFreindAccept(String id){
    	long senderId = Long.parseLong(id);
    	friendService.acceptInvitation(senderId, 
    			                       session.getCurrentUser().getId());
    	/*
    	for(FriendInvitation invitation : friendInvitations)
    		if(invitation.getFrom().getId() == Long.parseLong(id))
    			friendInvitations.remove(invitation);
    	*/
    	
    	Iterator<FriendInvitation> it = friendInvitations.iterator();
    	while(it.hasNext()){
    		FriendInvitation invitation = it.next();
    		if(invitation.getFrom().getId() == senderId){
    			it.remove();
    			break;
    		}
    	}
    	
    	User newFriend = userRepository.findById(senderId);
    	users.put(senderId, newFriend);
    }
	
	//////////////////////////////////////////////////// GET AND SET ///////////////////////////////////////////////////////////////////

	public GoogleMapSetting getSetting() {
		return setting;
	}

	public void setSetting(GoogleMapSetting setting) {
		this.setting = setting;
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getFriendNick() {
		return friendNick;
	}

	public void setFriendNick(String friendNick) {
		this.friendNick = friendNick;
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public List<FriendInvitation> getFriendInvitations() {
		return friendInvitations;
	}

	public void setFriendInvitations(List<FriendInvitation> friendInvitations) {
		this.friendInvitations = friendInvitations;
	}

	public boolean isGpsVisible() {
		return gpsVisible;
	}

	public void setGpsVisible(boolean gpsVisible) {
		this.gpsVisible = gpsVisible;
	}

	public boolean isNetworkVisible() {
		return networkVisible;
	}

	public void setNetworkVisible(boolean networkVisible) {
		this.networkVisible = networkVisible;
	}

	public boolean isOwnVisible() {
		return ownVisible;
	}

	public void setOwnVisible(boolean ownVisible) {
		this.ownVisible = ownVisible;
	}

	public Map<Long, User> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, User> users) {
		this.users = users;
	}
	
}
