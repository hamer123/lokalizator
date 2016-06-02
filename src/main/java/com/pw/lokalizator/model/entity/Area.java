package com.pw.lokalizator.model.entity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.pw.lokalizator.model.enums.AreaFollows;

@Entity
@Table(name="area")
@NamedQueries(value={
		      @NamedQuery(name="Area.findByTargetId", 
		    		      query="SELECT a FROM Area a WHERE a.target.id = :id"),
		      @NamedQuery(name="Area.removeById",
		                  query="DELETE FROM Area a WHERE a.id = :id"),
		      @NamedQuery(name="Area.findAll",
		                  query="SELECT a FROM Area a"),
		      @NamedQuery(name="Area.findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId",
		                  query="SELECT new com.pw.lokalizator.model.entity.Area(a.id, a.name, a.polygonFollowType, t.id, t.login) FROM Area a INNER JOIN a.target t WHERE a.provider.id =:id"),
		      @NamedQuery(name="Area.findWithEagerFetchPointsAndTargetByProviderId",
		                  query="SELECT a FROM Area a JOIN FETCH a.target WHERE a.provider.id =:id")
})
public class Area {
    @TableGenerator
    (
       name="areaGenerator", 
       table="ID_GEN", 
       pkColumnName="GEN_KEY", 
       valueColumnName="GEN_VALUE", 
       pkColumnValue="AREA_ID", 
       allocationSize=1
    )
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="areaGenerator")
	private long id;
    
	@NotNull
	@Size(min = 4, max = 16)
    @Column
	private String name;
    
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@MapKey(name="number")
	private Map<Integer,PolygonPoint>points = new HashMap<Integer, PolygonPoint>();
	
	@NotNull
	@OneToOne
	@JoinColumn(unique = false)
	private User target;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User provider;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AreaFollows polygonFollowType;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {})
	private List<AreaEventNetwork>areaEventNetworks;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {})
	private List<AreaEventGPS>areaEventGPSs;
	
	public Area(){}
	
	public Area(long id, String name, AreaFollows polygonFollows, long targetID, String targetLogin){
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
	public AreaFollows getPolygonFollowType() {
		return polygonFollowType;
	}
	public void setPolygonFollowType(AreaFollows polygonFollowType) {
		this.polygonFollowType = polygonFollowType;
	}

	public List<AreaEventNetwork> getAreaEventNetworks() {
		return areaEventNetworks;
	}

	public void setAreaEventNetworks(List<AreaEventNetwork> areaEventNetworks) {
		this.areaEventNetworks = areaEventNetworks;
	}

	public List<AreaEventGPS> getAreaEventGPSs() {
		return areaEventGPSs;
	}

	public void setAreaEventGPSs(List<AreaEventGPS> areaEventGPSs) {
		this.areaEventGPSs = areaEventGPSs;
	}
	
}
