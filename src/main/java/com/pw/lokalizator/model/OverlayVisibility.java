package com.pw.lokalizator.model;

public class OverlayVisibility {
	
	private boolean circleVisible = true;
	private boolean markerVisible = true;
	private boolean polylineVisible = true;
	private boolean rectangleVisibel = true;
	
	public OverlayVisibility(){
		
	}
	
	private OverlayVisibility(boolean circleVisible, boolean markerVisible, boolean polylineVisible, boolean rectangleVisibel){
		this.circleVisible = circleVisible;
		this.markerVisible = markerVisible;
		this.polylineVisible = polylineVisible;
		this.rectangleVisibel = rectangleVisibel;
	}
	
	public boolean isCircleVisible() {
		return circleVisible;
	}
	public void setCircleVisible(boolean circleVisible) {
		this.circleVisible = circleVisible;
	}
	public boolean isMarkerVisible() {
		return markerVisible;
	}
	public void setMarkerVisible(boolean markerVisible) {
		this.markerVisible = markerVisible;
	}
	public boolean isPolylineVisible() {
		return polylineVisible;
	}
	public void setPolylineVisible(boolean polylineVisible) {
		this.polylineVisible = polylineVisible;
	}
	public boolean isRectangleVisibel() {
		return rectangleVisibel;
	}
	public void setRectangleVisibel(boolean rectangleVisibel) {
		this.rectangleVisibel = rectangleVisibel;
	}
	
	@Override
	public String toString() {
		return "OverlayVisibility [circleVisible=" + circleVisible
				+ ", markerVisible=" + markerVisible + ", polylineVisible="
				+ polylineVisible + ", rectangleVisibel=" + rectangleVisibel
				+ "]";
	}

	public static class OverlayVisibilityBuilder{
		private boolean circleVisible;
		private boolean markerVisible;
		private boolean polylineVisible;
		private boolean rectangleVisibel;
		
		public OverlayVisibilityBuilder circleVisible(boolean circleVisible){
			this.circleVisible = circleVisible;
			return this;
		}
		
		public OverlayVisibilityBuilder markerVisible(boolean markerVisible){
			this.markerVisible = markerVisible;
			return this;
		}
		
		public OverlayVisibilityBuilder polylineVisible(boolean polylineVisible){
			this.polylineVisible = polylineVisible;
			return this;
		}
		
		public OverlayVisibilityBuilder rectangleVisibel(boolean rectangleVisibel){
			this.rectangleVisibel = rectangleVisibel;
			return this;
		}
		
		public OverlayVisibility build(){
			return new OverlayVisibility(
					   circleVisible,
					   markerVisible,
					   polylineVisible,
					   rectangleVisibel
					);
		}
	}
}
