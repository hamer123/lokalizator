package com.pw.lokalizator.model.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
		      @NamedQuery(name="Polygon.findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId",
		                  query="SELECT new com.pw.lokalizator.model.entity.PolygonModel(p.id, p.name, p.polygonFollowType, t.id, t.login) FROM PolygonModel p INNER JOIN p.target t WHERE p.provider.id =:id"),
		      @NamedQuery(name="Polygon.findWithEagerFetchPointsAndTargetByProviderId",
		                  query="SELECT p FROM PolygonModel p JOIN FETCH p.target WHERE p.provider.id =:id")
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
    
    @Column
	private String name;
    
	@OneToMany(mappedBy="polygon", fetch=FetchType.LAZY, cascade= { CascadeType.REMOVE, CascadeType.PERSIST })
	@MapKey(name="number")
	private Map<Integer, PolygonPoint>points;
	
	@OneToOne(optional = false)
	private User target;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User provider;
	
	@Enumerated(EnumType.STRING)
	private PolygonFollows polygonFollowType;
	
	public PolygonModel(){}
	
	public PolygonModel(long id, String name, PolygonFollows polygonFollows, long targetID, String targetLogin){
		this.id = id;
		this.name = name;
		this.polygonFollowType = polygonFollows;
		
		User target = new User();
		target.setId(targetID);
		target.setLogin(targetLogin);
		
		this.target = target;
	}
	
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
