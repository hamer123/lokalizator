package com.pw.lokalizator.model.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.pw.lokalizator.model.enums.PolygonFollows;

@Entity
@Table(name="polygon")
@NamedQueries(value={
		      @NamedQuery(name="PolygonModel.getPolygonsByTargetId", 
		    		      query="SELECT p FROM PolygonModel p WHERE p.target.id = :id"),
		      @NamedQuery(name="PolygonModel.removeById",
		                  query="DELETE FROM PolygonModel p WHERE p.id = :id"),
		      @NamedQuery(name="PolygonModel.findAll",
		                  query="SELECT p FROM PolygonModel p"),
})
public class PolygonModel {
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
	@OneToOne
	private User provider;
	@Enumerated(EnumType.STRING)
	private PolygonFollows polygonFollowType;
	
	public PolygonModel(){}
	
	

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
	public PolygonFollows getPolygonFollowType() {
		return polygonFollowType;
	}
	public void setPolygonFollowType(PolygonFollows polygonFollowType) {
		this.polygonFollowType = polygonFollowType;
	}
}
