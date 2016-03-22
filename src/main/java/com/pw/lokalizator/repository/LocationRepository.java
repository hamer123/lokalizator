package com.pw.lokalizator.repository;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;

@Local
public interface LocationRepository extends JpaRepository<Location, Long> {
	long count();
}
