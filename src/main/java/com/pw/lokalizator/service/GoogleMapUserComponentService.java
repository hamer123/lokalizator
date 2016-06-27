package com.pw.lokalizator.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import com.pw.lokalizator.model.GoogleMapComponentVisible;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;

@Local
public interface GoogleMapUserComponentService {
	List<Overlay> lastLocation(User user);
	List<Overlay> lastLocation(Set<User> users);
	List<Overlay> lastLocation(User user,  GoogleMapComponentVisible visible);
	List<Overlay> lastLocation(Set<User> users,  GoogleMapComponentVisible visible);
	
	List<Overlay> route(List<Location>locations);
}
