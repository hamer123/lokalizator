package com.pw.lokalizator.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;

@Local
public interface LocationRepository extends JpaRepository<Location, Long> {
	long count();
	List<Location>getLocationYoungThanAndOlderThan(Date younger, Date older);
}
