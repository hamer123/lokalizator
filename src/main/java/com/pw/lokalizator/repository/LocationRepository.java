package com.pw.lokalizator.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;

@Local
public interface LocationRepository extends JpaRepository<Location, Long> {
	long count();
	List<Location>getLocationYoungThanAndOlderThan(Date younger, Date older);
	Location getFromUser(long id);
	List<Location> getFromUser(Set<Long>ids);
	List<Location> getByUsersId(Set<Long>gps, Set<Long>network, Set<Long>own);
	
	Map<Long,Location> getGpsByUserId(Set<Long>gps);
	Map<Long,Location> getNetworkByUserId(Set<Long>gps);
	Map<Long,Location> getOwnByUserId(Set<Long>gps);
	List<Location> findWhereCityIsNull();
	int updateCity(double lat, double lon, String address);
}
