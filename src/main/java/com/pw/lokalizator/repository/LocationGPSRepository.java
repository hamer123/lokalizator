package com.pw.lokalizator.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.LocationGPS;

@Local
public interface LocationGPSRepository extends JpaRepository<LocationGPS, Long>{

	List<LocationGPS> findByUserLoginAndDateOrderByDateDesc(String login, Date younger, Date older);
}
