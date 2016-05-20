package com.pw.lokalizator.repository;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.WifiInfo;

@Local
public interface WifiInfoRepository extends JpaRepository<WifiInfo, Long> {

	WifiInfo findByLocationId(Long id);
}
