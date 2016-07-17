package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserSetting implements Serializable{
	@Column(name = "default_latitude")
	private double defaultLatitude;
	@Column(name = "default_longtitude")
	private double defaultLongtitude;
	@Column(name = "g_map_zoom")
	private int gMapZoom;
	
	
	public double getDefaultLatitude() {
		return defaultLatitude;
	}
	public void setDefaultLatitude(double defaultLatitude) {
		this.defaultLatitude = defaultLatitude;
	}
	public double getDefaultLongtitude() {
		return defaultLongtitude;
	}
	public void setDefaultLongtitude(double defaultLongtitude) {
		this.defaultLongtitude = defaultLongtitude;
	}
	public int getgMapZoom() {
		return gMapZoom;
	}
	public void setgMapZoom(int gMapZoom) {
		this.gMapZoom = gMapZoom;
	}
}
