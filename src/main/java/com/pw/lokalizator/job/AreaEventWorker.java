package com.pw.lokalizator.job;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.model.entity.AreaMessageMail;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.AreaPoint;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.model.enums.AreaMailMessageModes;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.singleton.RestSessionManager;

@Stateless
@LocalBean
public class AreaEventWorker {
	@Inject
	private RestSessionManager restSessionManager;
	@Inject
	private AreaRepository areaRepository;
	@Inject
	private Logger logger;
	@PersistenceContext
	private EntityManager em;

	@Schedule(minute="*/1", hour="*")
	public void job(){
		logger.info("[AreaEventService] Jobe has started");
		long time = System.currentTimeMillis();

		work();
		
		logger.info("[AreaEventService] Jobe has ended after " + (System.currentTimeMillis() - time) + "ms");
	}
	
	private void work(){
		List<Area>areas = findAllArea();
		
		for(Area area : areas){
			if(shouldCheckArea(area)){
				Path2D path2d = createPathFromAreaPoints(area.getPoints());
				List<AreaEvent> areaEventList = null;
				
				if(area.getAreaFollowType() == AreaFollows.OUTSIDE){
					areaEventList = createAreaEventListForOutisdeFollow(path2d, area);
					saveAreaEvents(areaEventList);
				} else if(area.getAreaFollowType() == AreaFollows.INSIDE){
					areaEventList = createAreaEventListForInsideFollow(path2d, area);
					saveAreaEvents(areaEventList);					
				}
				
				if(!areaEventList.isEmpty()){
					updateAreaMessageMailAccept(area);
					updateLocationEventCheck(area);
				}
			}
		}
	}
	
	private void updateAreaMessageMailAccept(Area area){
		if(shouldChangeMessageMail(area)){
			area.getAreaMessageMail().setAccept(false);
		}
	}
	
	private void updateLocationEventCheck(Area area){
		User user = area.getTarget();
		
		if(user.getLastLocationGPS() != null)
			user.getLastLocationGPS().setEventCheck(true);
		
		if(user.getLastLocationNetworkNaszaUsluga() != null)
			user.getLastLocationNetworkNaszaUsluga().setEventCheck(true);
		
		if(user.getLastLocationNetworObcaUsluga() != null)
			user.getLastLocationNetworObcaUsluga().setEventCheck(true);
	}
	
	private boolean shouldChangeMessageMail(Area area){
		return area.getAreaMessageMail().getAreaMailMessageMode() == AreaMailMessageModes.ACCEPT;
	}
	
	private List<AreaEvent> createAreaEventListForInsideFollow(Path2D path2d, Area area){
		List<AreaEvent>areaEventList = new ArrayList<AreaEvent>();
		User user = area.getTarget();
		
		Location location = user.getLastLocationGPS();
		if( validateLocation(location) && !isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationGPS()) );
		
		location = user.getLastLocationNetworkNaszaUsluga();
		if( validateLocation(location) && !isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworkNaszaUsluga()) );
		
		location = user.getLastLocationNetworObcaUsluga();
		if( validateLocation(location) && !isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworObcaUsluga()) );
		
		return areaEventList;
	}
	
	private List<AreaEvent> createAreaEventListForOutisdeFollow(Path2D path2d, Area area){
		List<AreaEvent>areaEventList = new ArrayList<AreaEvent>();
		User user = area.getTarget();
		
		Location location = user.getLastLocationGPS();
		if( validateLocation(location) && isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationGPS()) );
		
		location = user.getLastLocationNetworkNaszaUsluga();
		if( validateLocation(location) && isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworkNaszaUsluga()) );
		
		location = user.getLastLocationNetworObcaUsluga();
		if( validateLocation(location) && isPathContaintLocation(path2d, location) )
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworObcaUsluga()) );
		
		return areaEventList;
	}
	
	private boolean validateLocation(Location location){
		return location != null && !location.isEventCheck();
	}
	
	private AreaEvent createAreaEvent(Area area, Location location){
		AreaEvent areaEvent = getAreaEventBasedOnLocationType(location);

		areaEvent.setArea(area);
		areaEvent.setDate(new Date());
		areaEvent.setMailSend( shouldSendMailMessage( area.getAreaMessageMail() ) );
		areaEvent.setMessage( createAreaEventMessage(area, location) );
		return areaEvent;
	}
	
	private String createAreaEventMessage(Area area, Location location){
		StringBuilder builder = new StringBuilder();
		
		builder.append(area.getTarget().getLogin())
		       .append( getMessagePartFollowType(area) )
		       .append(" dnia ")
		       .append(location.getDate())
		       .append(" (lokalizacja za pomoca ")
		       .append(getMessagePartLocationProvider(location))
		       .append(" )");
		
		return builder.toString();
	}
	
	private String getMessagePartLocationProvider(Location location){
		String provider = location.getProviderType().toString();
		
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			provider += " ";
			provider += locationNetwork.getLocalizationServices();
		}
		
		return provider;
	}
	
	private String getMessagePartFollowType(Area area){
		if(area.getAreaFollowType() == AreaFollows.INSIDE)
			return " opuscil obszar sledzenia"
			       + area.getName();
		else
			return " wszedl do obszaru sledzenia"
		           + area.getName();
	}
	
	private boolean shouldSendMailMessage(AreaMessageMail areaMessageMail){
		if( areaMessageMail.isActive() ){
			if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageModes.ACCEPT && areaMessageMail.isAccept()){
				return true;
			} else if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageModes.EVER ){
				return true;
			}
		}
		
		return false;
	}
	
	private void saveAreaEvents(List<AreaEvent>areaEventList){
		for(AreaEvent areaEvent : areaEventList)
			em.persist(areaEvent);
	}
	
	private AreaEvent getAreaEventBasedOnLocationType(Location location){
		if(location instanceof LocationNetwork)
			return  new AreaEventNetwork((LocationNetwork)location);
		else if(location instanceof LocationGPS)
		    return new AreaEventGPS((LocationGPS)location);
		else
			throw new IllegalArgumentException
			("[AreaEventService] Nie poprawy arguemnt, ta lokacja nie jest oblusgiwana " + location.getClass());	
	}
	
	private boolean isPathContaintLocation(Path2D path2d, Location location){
		Point2D point2d = new Point2D.Double(location.getLatitude(), location.getLongitude());
		return path2d.contains(point2d);
	}	
	
	private List<Area> findAllArea(){
		return areaRepository.findAll();
	}
	
	private boolean shouldCheckArea(Area area){
		return isUserOnline(area.getTarget()) &&
			   area.isAktywny();
	}
	
	private boolean isUserOnline(User user){
		String login = user.getLogin();
		return restSessionManager.isUserOnline(login);
	}
	
	private Path2D createPathFromAreaPoints(Map<Integer, AreaPoint>points){
		Path2D path = new Path2D.Double();
		
		AreaPoint firstPoint = points.get(1);
		path.moveTo(firstPoint.getLat(), firstPoint.getLng());
		
		for(int index = 1; index < points.size(); index++){
			AreaPoint point = points.get(index);
			path.lineTo(point.getLat(), point.getLng());
		}		
		
		return path;
	}
}
