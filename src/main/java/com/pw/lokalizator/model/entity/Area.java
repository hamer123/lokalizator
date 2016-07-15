package com.pw.lokalizator.model.entity;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
	          @NamedQuery(name="Area.findByProviderId", 
    		              query="SELECT a FROM Area a WHERE a.provider.id = :id"),
		      @NamedQuery(name="Area.findByTargetId", 
		    		      query="SELECT a FROM Area a WHERE a.target.id = :id"),
		      @NamedQuery(name="Area.removeById",
		                  query="DELETE FROM Area a WHERE a.id = :id"),
		      @NamedQuery(name="Area.findAll",
		                  query="SELECT a FROM Area a"),
//		      @NamedQuery(name="Area.findIdAndNameAndFollowTypeAndTargetIdAndTargetLoginByProviderId",
//		                  query="SELECT new com.pw.lokalizator.model.entity.Area(a.id, a.name, a.polygonFollowType, t.id, t.login) FROM Area a INNER JOIN a.target t WHERE a.provider.id =:id"),
		      @NamedQuery(name="Area.findWithEagerFetchPointsAndTargetByProviderId",
		                  query="SELECT a FROM Area a JOIN FETCH a.target WHERE a.provider.id =:id"),
		      @NamedQuery(name="Area.findByAktywny",
		    		      query="SELECT a FROM Area a WHERE a.aktywny =:aktywny"),
		      @NamedQuery(name="Area.findIdByProviderIdAndAktywny", 
		                  query="SELECT a.id FROM Area a WHERE a.provider.id =:id AND a.aktywny =:active")
})
public class Area {
	public static final String AREA_updateAktywnyById = "UPDATE Area a SET a.aktywny =:aktywny WHERE a.id =:id";
	
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
    
	@NotNull
	@ManyToOne
	@JoinColumn(unique = false)
	private User target;
	
	@NotNull
	@ManyToOne
	private User provider;
	
	@Column(name = "active")
	private boolean aktywny;
	
	@Column(name = "color")
	private String color;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AreaFollows polygonFollowType;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {CascadeType.REMOVE})
	private List<AreaEventNetwork>areaEventNetworks;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {CascadeType.REMOVE})
	private List<AreaEventGPS>areaEventGPSs;
	
	@NotNull
	@OneToOne(orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private AreaMessageMail areaMessageMail;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@MapKey(name="number")
	private Map<Integer,AreaPoint>points = new HashMap<Integer, AreaPoint>();
	
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
	public Map<Integer, AreaPoint> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer, AreaPoint> points) {
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
	public AreaFollows getAreaFollowType() {
		return polygonFollowType;
	}
	public void setAreaFollowType(AreaFollows polygonFollowType) {
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

	public boolean isAktywny() {
		return aktywny;
	}

	public void setAktywny(boolean aktywny) {
		this.aktywny = aktywny;
	}

	public AreaFollows getPolygonFollowType() {
		return polygonFollowType;
	}

	public void setPolygonFollowType(AreaFollows polygonFollowType) {
		this.polygonFollowType = polygonFollowType;
	}

	public AreaMessageMail getAreaMessageMail() {
		return areaMessageMail;
	}

	public void setAreaMessageMail(AreaMessageMail areaMessageMail) {
		this.areaMessageMail = areaMessageMail;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public boolean contains(Location location){
		Path2D path2d = createPath(getPoints());
		Point2D point2d = new Point2D.Double(location.getLatitude(), location.getLongitude());
		return path2d.contains(point2d);
	}
	
	private Path2D createPath(Map<Integer, AreaPoint>points){
		Path2D path = new Path2D.Double();
		
		AreaPoint firstPoint = points.get(0);
		path.moveTo(firstPoint.getLat(), firstPoint.getLng());
		
		for(int index = 1; index < points.size(); index++){
			AreaPoint point = points.get(index);
			path.lineTo(point.getLat(), point.getLng());
		}		
		
		return path;
	}
}
