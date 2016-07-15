package com.pw.lokalizator.service;

import javax.ejb.Local;

import com.pw.lokalizator.model.Route;

@Local
public interface RouteService {

	double calculateLenghtMeters(Route route);
}
