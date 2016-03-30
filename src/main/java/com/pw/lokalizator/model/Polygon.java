package com.pw.lokalizator.model;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

@Entity
public class Polygon {
    @TableGenerator(
            name="polGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="POL_ID", 
            allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="polGen")
	private long id;
	private String name;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="polygon")
	@MapKey(name="number")
	private Map<Integer, PolygonPoint>points;
	@OneToOne
	private User target;
	@ManyToOne
	private User provider;
	@Enumerated
	private PolygonType polygonType;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Integer, PolygonPoint> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer, PolygonPoint> points) {
		this.points = points;
	}
	public User getTarget() {
		return target;
	}
	public void setTarget(User target) {
		this.target = target;
	}
	public User getProvider() {
		return provider;
	}
	public void setProvider(User provider) {
		this.provider = provider;
	}
	public PolygonType getPolygonType() {
		return polygonType;
	}
	public void setPolygonType(PolygonType polygonType) {
		this.polygonType = polygonType;
	}

}
