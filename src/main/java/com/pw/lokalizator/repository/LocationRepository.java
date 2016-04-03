package com.pw.lokalizator.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;

@Local
public interface LocationRepository extends JpaRepository<Location, Long> {
	long count();
	List<Location>getLocationYoungThanAndOlderThan(Date younger, Date older);
	Location getFromUser(long id);
	List<Location> getFromUser(Set<Long>ids);
}
