package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.PolygonModel;

@Local
public interface PolygonModelRepository extends JpaRepository<PolygonModel, Long> {
	List<PolygonModel> findByTargetId(long id);
	List<PolygonModel> findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId(long id);
	List<PolygonModel> findWithEagerFetchPointsAndTargetByProviderId(long id);
}
