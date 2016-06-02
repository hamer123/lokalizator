package com.pw.lokalizator.job;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
















import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.PolygonPoint;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.singleton.RestSessionManager;

@Stateless
@LocalBean
public class AreaEventService {
	@Inject
	private RestSessionManager restSessionManager;
	
	@Inject
	private AreaRepository areaRepository;

	@PersistenceContext
	private EntityManager em;
	
	private Logger logger = Logger.getLogger(AreaEventService.class);

	@Schedule(minute="*/1", hour="*")
	public void jobe(){
		logger.info("[AreaEventService] Jobe has started");
		long time = System.currentTimeMillis();
		
		jobeWork();
		
		logger.info("[AreaEventService] Jobe has ended after " + (System.currentTimeMillis() - time) + "ms");
	}
	
	private void jobeWork(){
		List<Area>areaList = findAllArea();
		
		for(Area area : areaList){
			if(isUserOnline(area.getTarget())){
				Path2D path2d = createPathFromAreaPoints(area.getPoints());
				
				if(area.getPolygonFollowType() == AreaFollows.OUTSIDE){
					List<AreaEvent> areaEventList = createAreaEventListForOutisdeFollow(path2d, area);
					saveAreaEvents(areaEventList);
				} else if(area.getPolygonFollowType() == AreaFollows.INSIDE){
					List<AreaEvent> areaEventList = createAreaEventListForInsideFollow(path2d, area);
					saveAreaEvents(areaEventList);
				}
				
			}
		}
	}
	
	private void saveAreaEvents(List<AreaEvent>areaEventList){
		for(AreaEvent areaEvent : areaEventList)
			em.persist(areaEvent);
	}
	
	private List<AreaEvent> createAreaEventListForInsideFollow(Path2D path2d, Area area){
		List<AreaEvent>areaEventList = new ArrayList<AreaEvent>();
		User user = area.getTarget();
		
		Location location = user.getLastLocationGPS();
		if(location != null && !isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationGPS()) );
		
		location = user.getLastLocationNetworkNaszaUsluga();
		if(location != null && !isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworkNaszaUsluga()) );
		
		location = user.getLastLocationNetworObcaUsluga();
		if(location != null && !isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworObcaUsluga()) );
		
		return areaEventList;
	}
	
	private List<AreaEvent> createAreaEventListForOutisdeFollow(Path2D path2d, Area area){
		List<AreaEvent>areaEventList = new ArrayList<AreaEvent>();
		User user = area.getTarget();
		
		Location location = user.getLastLocationGPS();
		if(location != null && isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationGPS()) );
		
		location = user.getLastLocationNetworkNaszaUsluga();
		if(location != null && isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworkNaszaUsluga()) );
		
		location = user.getLastLocationNetworObcaUsluga();
		if(location != null && isPathContaintLocation(path2d, location))
			areaEventList.add( createAreaEvent(area, user.getLastLocationNetworObcaUsluga()) );
		
		return areaEventList;
	}
	
	private AreaEvent createAreaEvent(Area area, Location location){
		AreaEvent areaEvent = getAreaEventBasedOnLocationType(location);

		areaEvent.setArea(area);
//		areaEvent.setMessage(null); //TODO? nie wiem na chuj nam message :D
		
		return areaEvent;
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
	
	private boolean isUserOnline(User user){
		String login = user.getLogin();
		return restSessionManager.isUserOnline(login);
	}
	
	private Path2D createPathFromAreaPoints(Map<Integer, PolygonPoint>points){
		Path2D path = new Path2D.Double();
		
		PolygonPoint firstPoint = points.get(0);
		path.moveTo(firstPoint.getLat(), firstPoint.getLng());
		
		for(int index = 1; index < points.size(); index++){
			PolygonPoint point = points.get(index);
			path.lineTo(point.getLat(), point.getLng());
		}		
		
		return path;
	}
}
