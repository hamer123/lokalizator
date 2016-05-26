package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.PolygonPoint;

@Local
public interface PolygonPointRepository extends JpaRepository<PolygonPoint, Long>{
	List<PolygonPoint> findByPolygonModelId(long id);
}
