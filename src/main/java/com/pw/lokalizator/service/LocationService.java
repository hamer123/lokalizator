package com.pw.lokalizator.service;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;

@Local
public interface LocationService {

	void createLocationNetworkUpdateUserCurrentLocationNetwork(LocationNetwork locationNetwork, long id);
	void createLocationGPSUpdateUserCurrentLocationGPS(LocationGPS locationGPS, long id);
}
