package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.PolygonModel;

@Local
public interface PolygonModelRepository extends JpaRepository<PolygonModel, Long> {
	List<PolygonModel> getPolygonsByTargetId(Long id);
}
