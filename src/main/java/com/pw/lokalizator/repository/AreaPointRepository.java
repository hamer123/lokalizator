package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.AreaPoint;

@Local
public interface AreaPointRepository extends JpaRepository<AreaPoint, Long>{
	List<AreaPoint> findByAreaId(long id);
}
