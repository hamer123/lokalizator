package com.pw.lokalizator.repository;

import javax.ejb.Local;

import com.pw.lokalizator.model.CurrentLocation;

@Local
public interface CurrentLocationRepository extends JpaRepository<CurrentLocation, Long>{

}
