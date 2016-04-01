package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.CurrentLocation;

@Local
public interface CurrentLocationRepository extends JpaRepository<CurrentLocation, Long>{
	public CurrentLocation findByUserId(long id);
	public List<CurrentLocation> findByUsersId(Set<Long>ids);
}
