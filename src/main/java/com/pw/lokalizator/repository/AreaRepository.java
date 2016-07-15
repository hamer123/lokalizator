package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.Area;

@Local
public interface AreaRepository extends JpaRepository<Area, Long> {
	List<Area> findByTargetId(long id);
	List<Area> findByProviderId(long id);
	List<Area> findWithEagerFetchPointsAndTargetByProviderId(long id);
	List<Area>finbByActive(boolean active);
	List<Long>findIdByProviderIdAndActive(long id, boolean active);
	int updateAktywnyById(boolean aktywny, long id);
	void removeById(long id);
}
