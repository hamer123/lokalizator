package com.pw.lokalizator.service;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.User;

@Local
public interface UserLastLocationsService {

	void updateUserLastLocation(User user);
}
