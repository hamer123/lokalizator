package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;

@Local
public interface AreaEventNetworkRepository extends JpaRepository<AreaEventNetwork, Long>{
	public List<AreaEventNetwork> findByAreaId(long id);
}
