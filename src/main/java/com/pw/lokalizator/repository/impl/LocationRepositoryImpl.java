package com.pw.lokalizator.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.LocationRepository;


@Stateless
public class LocationRepositoryImpl implements LocationRepository{
	Logger LOG = Logger.getLogger(LocationRepositoryImpl.class);
	@PersistenceContext
	EntityManager em;
	
	Logger log = Logger.getLogger(LocationRepositoryImpl.class);
	
	public Location create(Location entity) {
		em.persist(entity);
		return entity;
	}

	public Location save(Location entity) {
		return em.merge(entity);
	}

	public void remove(Location entity) {
		em.remove(entity);
	}

	public void remove(Long id) {
		em.createNamedQuery("Location.deleteById", Location.class).executeUpdate();
	}

	public Location findById(Long id) {
		return em.find(Location.class, id);
	}

	public List<Location> findAll() {
		return em.createQuery("SELECT l FROM Location l", Location.class).getResultList();
	}

	public long count() {
		return em.createNamedQuery("Location.count").getFirstResult();
	}

	/*
	@Override
	public List<Location> getLocationYoungThanAndOlderThan(Date younger, Date older) {
		return em.createNamedQuery("Location.findOlderThanAndYoungerThan", Location.class)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .getResultList();
	}

	@Override
	public Location getFromUser(long id) {
		return em.createNamedQuery("Location.findFromUser", Location.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}

	@Override
	public List<Location> getFromUser(Set<Long> ids) {
		return em.createNamedQuery("Location.findFromUsers", Location.class)
				.setParameter("ids", ids)
				.getResultList();
	}

	@Override
	public List<Location> getByUsersId(Set<Long> gpsId, Set<Long>networkId, Set<Long>ownId) {
		
			   em.createNamedQuery("Location.Native.findByUserIds")
				 .setParameter("gps_id", gpsId)
				 .setParameter("network_id", networkId)
				 .setParameter("own_id", ownId)
				 .getResultList();
			   return null;
	}

	@Override
	public Map<Long,Location> getGpsByUserId(Set<Long> gps) {
		long time = System.currentTimeMillis();
		Map<Long,Location>result = null;
		
		try{
			
			@SuppressWarnings("unchecked")
			List<Object[]> results = (List<Object[]>) em
					 .createNamedQuery("Location.Native.findGps")
					 .setParameter("id", gps)
					 .getResultList();
			
			if(results.size() > 0){
				result = new HashMap<Long,Location>();
				for(Object[] obj : results){
					result.put(((BigInteger) obj[3]).longValue(),
							   new Location((Date)obj[0], (double)obj[1], (double)obj[2]) );
				}
					
			}
		}catch(Exception pe){
			log.error("Exception podczac selecta locations dla GPS", pe);
		}
		log.info("Select location dla GPS trwal " + (System.currentTimeMillis() - time) + " ms dla " + result.size() + " rekordow");
		return result;
	}

	@Override
	public Map<Long,Location> getNetworkByUserId(Set<Long> network) {
		long time = System.currentTimeMillis();
		Map<Long,Location>result = null;
		
		try{
			
			@SuppressWarnings("unchecked")
			List<Object[]> results = (List<Object[]>) em
					 .createNamedQuery("Location.Native.findNetwork")
					 .setParameter("id", network)
					 .getResultList();
			
			if(results.size() > 0){
				result = new HashMap<Long,Location>();
				for(Object[] obj : results){
					result.put(((BigInteger) obj[3]).longValue(),
							   new Location((Date)obj[0], (double)obj[1], (double)obj[2]) );
				}
					
			}
		}catch(Exception pe){
			log.error("Exception podczac selecta locations dla Network", pe);
		}
		log.info("Select location dla Network trwal " + (System.currentTimeMillis() - time) + " ms dla " + result.size() + " rekordow");
		return result;
	}

	@Override
	public Map<Long,Location> getOwnByUserId(Set<Long> own) {
		long time = System.currentTimeMillis();
		Map<Long,Location>result = null;
		
		try{
			
			@SuppressWarnings("unchecked")
			List<Object[]> results = (List<Object[]>) em
					 .createNamedQuery("Location.Native.findOwn")
					 .setParameter("id", own)
					 .getResultList();
			
			if(results.size() > 0){
				result = new HashMap<Long,Location>();
				for(Object[] obj : results){
					result.put(((BigInteger) obj[3]).longValue(),
							   new Location((Date)obj[0], (double)obj[1], (double)obj[2]) );
				}
					
			}
		}catch(Exception pe){
			log.error("Exception podczac selecta locations dla Own", pe);
		}
		log.info("Select location dla Own trwal " + (System.currentTimeMillis() - time) + " ms dla " + result.size() + " rekordow");
		return result;
	}

	@Override
	public List<Location> findWhereCityIsNull() {
		long time = System.currentTimeMillis();
		List<Location>locations = null;
		
		try{
			locations = em
					    .createNamedQuery("Location.findToUpdateAddress", Location.class)
					    .setMaxResults(250)  
			            .getResultList();
		}catch(Exception e){
			log.error("Exception podczac selecta location dla pustych adresow", e);
		}
		
		log.info("Select location dla pustych adresow trwal " + (System.currentTimeMillis() - time) + " ms dla " + locations.size() + " rekordow");
		return locations;
	}

	@Override
	public int updateCity(double lat, double lon, String address) {
		int count = 0;
		try{
			count += em
					   .createNamedQuery("Location.Native.updateCity")
					   .setParameter("address", address)
					   .setParameter("lat", lat)
					   .setParameter("lon", lon)
					   .executeUpdate();
		} catch(Exception e){
			log.error("Update nie udal sie ", e);
		}
		return count;
	}

	@Override
	public List<Location> getLastLocationsByUserId(Long id) {
		return null;
	}

	@Override
	public List<Location> findLastUserLocationByLogin(String login) {
		List<Object[]>result = em.createNamedQuery("Location.Native.findLastUserLocationsByLogin")
				                 .setParameter("login", login)
				                 .getResultList();
		
		List<Location>locations = new ArrayList<Location>();
		
		for(Object[] obj : result){
			Location location = new Location();
			location.setId(((BigInteger)obj[0]).longValue());
			location.setAddress((String)obj[1]);
			location.setDate((Date)obj[2]);
			location.setLatitude((double)obj[3]);
			location.setLongitude((double)obj[4]);
			location.setProvider(Providers.values()[(int)obj[5]]);
			
			User user = new User();
			user.setId(((BigInteger)obj[6]).longValue());
			user.setLogin((String)obj[7]);
			
			location.setUser(user);
			
			locations.add(location);
		}
		return locations;
	}
	*/
	
	@Override
	public List<Location> findWhereAddressIsNull() {
		long time = System.currentTimeMillis();
		List<Location>locations = null;
		
		try{
			locations = em
					    .createNamedQuery("Location.findWhereAddressIsNull", Location.class)
					    .setMaxResults(250)  
			            .getResultList();
		}catch(Exception e){
			log.error("Exception podczac selecta location dla pustych adresow", e);
		}
		
		log.info("Select location dla pustych adresow trwal " + (System.currentTimeMillis() - time) + " ms dla " + locations.size() + " rekordow");
		return locations;
	}
	
	
	
	@Override
	public List<Location> findByUserIdWhereYoungerThanAndOlderThanOrderByDateDesc(Long id,
			Date younger, Date older) {
		return em.createNamedQuery("", Location.class)
				 .setParameter("id", id)
				 .setParameter("older", older, TemporalType.TIMESTAMP)
				 .setParameter("younger", younger, TemporalType.TIMESTAMP)
				 .getResultList();
	}
	
}
