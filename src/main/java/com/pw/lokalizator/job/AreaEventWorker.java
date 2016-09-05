package com.pw.lokalizator.job;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import com.pw.lokalizator.jsf.utilitis.AreaEventBuilder;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.Location;
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

	private final AreaEventBuilder areaEventBuilder = new AreaEventBuilder();

	@Schedule(minute="*/1", hour="*")
	public void checkActiveAreaWithOnlineTarget(){
		List<Area>areas = findActiveArea();
		for(Area area : areas){
			if(shouldCheckArea(area)){
				User target = area.getTarget();
				List<Location>locations = validLocation(target);
				
				if(area.getAreaFollowType() == AreaFollows.INSIDE){
					for(Location location : locations)
						if(!area.contains(location))
							createAreaEventAndUpdateLocationEventCheck(location, area);
					
					if(shouldChangeMessageMail(area))
						area.getAreaMessageMail().setAccept(false);
				}else{
					for(Location location : locations)
						if(area.contains(location))
							createAreaEventAndUpdateLocationEventCheck(location, area);
					
					if(shouldChangeMessageMail(area))
						area.getAreaMessageMail().setAccept(false);
				}
			}
		}
	}
	
	@AroundTimeout
	public Object log(InvocationContext ic) throws Exception {
		long time = System.currentTimeMillis();
		try{
			logger.info(" job has started");
			return ic.proceed();
		} finally{
			logger.info(" job has ended after " + (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void createAreaEventAndUpdateLocationEventCheck(Location location, Area area){
		AreaEvent areaEvent = areaEventBuilder.create(area, location);
		location.setEventCheck(true);
		em.persist(areaEvent);
		areaEvent.setUrl(areaEventBuilder.createUrl(areaEvent));
	}
	
	private List<Area>findActiveArea(){
		return areaRepository.finbByActive(true);
	}
	
	private List<Location> validLocation(User user){
		List<Location>locations = new ArrayList<>();
		
		Location location = user.getLastLocationGPS();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworkNaszaUsluga();
		if(validateLocation(location)) locations.add(location);
		location = user.getLastLocationNetworObcaUsluga();
		if(validateLocation(location)) locations.add(location);
		
		return locations;
	}
	
	private boolean validateLocation(Location location){
		return location != null && !location.isEventCheck();
	}

	private boolean shouldCheckArea(Area area){
		return restSessionManager.isUserOnline(area.getTarget().getLogin()) &&
			   area.isAktywny();
	}
	
	private boolean shouldChangeMessageMail(Area area){
		return area.getAreaMessageMail().getAreaMailMessageMode() == AreaMailMessageModes.ACCEPT;
	}
}
