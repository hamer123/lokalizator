package com.pw.lokalizator.repository;

import java.math.BigInteger;
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

import com.pw.lokalizator.model.Location;


@Stateless
public class LocationRepositoryImpl implements LocationRepository{
	@PersistenceContext
	EntityManager em;
	
	Logger log = Logger.getLogger(LocationRepositoryImpl.class);
	
	public Location add(Location entity) {
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
		int result = em.createNamedQuery("Location.deleteById", Location.class).executeUpdate();
		if(result == 0)
			throw new IllegalArgumentException("nie mozna usunac Location o id = " + id + ", brak takiej encji");
	}

	public Location findById(Long id) {
		return em.find(Location.class, id);
	}

	public Collection<Location> findAll() {
		return em.createQuery("SELECT l FROM Location l", Location.class).getResultList();
	}

	public long count() {
		return em.createNamedQuery("Location.count").getFirstResult();
	}

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
			            .getResultList();
		}catch(Exception e){
			log.error("Exception podczac selecta location dla pustych adresow", e);
		}
		
		log.info("Select location dla pustych adresow trwal " + (System.currentTimeMillis() - time) + " ms dla " + locations.size() + " rekordow");
		return locations;
	}

}
