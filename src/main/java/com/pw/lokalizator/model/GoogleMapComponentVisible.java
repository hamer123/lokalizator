package com.pw.lokalizator.model;

public class GoogleMapComponentVisible {
	
	public static final GoogleMapComponentVisible NO_POLYGON = new GoogleMapComponentVisible();
	
	static
	{
		NO_POLYGON.setPolygon(false);
		NO_POLYGON.setCircleGps(true);
		NO_POLYGON.setCircleNetworkNasz(true);
		NO_POLYGON.setCircleNetworkObcy(true);
		NO_POLYGON.setMarkerGps(true);
		NO_POLYGON.setMarkerNetworkNasz(true);
		NO_POLYGON.setMarkerNetworkObcy(true);
	}
	
	private boolean polygon;
	private boolean markerGps;
	private boolean markerNetworkNasz;
	private boolean markerNetworkObcy;
	private boolean circleGps;
	private boolean circleNetworkNasz;
	private boolean circleNetworkObcy;
	
	public boolean isPolygon() {
		return polygon;
	}
	public void setPolygon(boolean polygon) {
		this.polygon = polygon;
	}
	public boolean isMarkerGps() {
		return markerGps;
	}
	public void setMarkerGps(boolean markerGps) {
		this.markerGps = markerGps;
	}
	public boolean isMarkerNetworkNasz() {
		return markerNetworkNasz;
	}
	public void setMarkerNetworkNasz(boolean markerNetworkNasz) {
		this.markerNetworkNasz = markerNetworkNasz;
	}
	public boolean isMarkerNetworkObcy() {
		return markerNetworkObcy;
	}
	public void setMarkerNetworkObcy(boolean markerNetworkObcy) {
		this.markerNetworkObcy = markerNetworkObcy;
	}
	public boolean isCircleGps() {
		return circleGps;
	}
	public void setCircleGps(boolean circleGps) {
		this.circleGps = circleGps;
	}
	public boolean isCircleNetworkNasz() {
		return circleNetworkNasz;
	}
	public void setCircleNetworkNasz(boolean circleNetworkNasz) {
		this.circleNetworkNasz = circleNetworkNasz;
	}
	public boolean isCircleNetworkObcy() {
		return circleNetworkObcy;
	}
	public void setCircleNetworkObcy(boolean circleNetworkObcy) {
		this.circleNetworkObcy = circleNetworkObcy;
	}

}
