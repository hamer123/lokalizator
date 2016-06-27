package com.pw.lokalizator.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.LocationNetwork;

@Local
public interface LocationNetworkRepository extends JpaRepository<LocationNetwork, Long> {

	List<LocationNetwork> findByUserLoginAndDateAndServiceEqualsNaszOrderByDateDesc(String login, Date younger, Date older);
	List<LocationNetwork> findByUserLoginAndDateAndServiceEqualsObcyOrderByDateDesc(String login, Date younger, Date older);
}
