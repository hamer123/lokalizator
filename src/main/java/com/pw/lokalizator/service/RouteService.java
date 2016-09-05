package com.pw.lokalizator.service;

import javax.ejb.Local;

import com.pw.lokalizator.model.google.component.Route;

@Local
public interface RouteService {

	double calculateLenghtMeters(Route route);
}
