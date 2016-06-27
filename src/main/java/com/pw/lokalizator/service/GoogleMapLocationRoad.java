package com.pw.lokalizator.service;

import java.util.List;

import javax.ejb.Local;

import org.primefaces.model.map.Overlay;

import com.pw.lokalizator.model.entity.Location;

@Local
public interface GoogleMapLocationRoad {

	public List<Overlay> createRout(List<Location>locations);
}
