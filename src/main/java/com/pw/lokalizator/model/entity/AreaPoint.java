package com.pw.lokalizator.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="polygonpoint")
@NamedQueries(value={
		@NamedQuery(name="PolygonPoint.findByPolygonModelId",
				    query="SELECT pp FROM AreaPoint pp WHERE pp.area.id =:id")
})
public class AreaPoint {
    @TableGenerator(
            name="ppGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="PP_ID"
            )
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ppGen")
	private long id;
    
	private int number;
	
	private double lat;
	
	private double lng;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Area area;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area polygon) {
		this.area = polygon;
	}
	
}
