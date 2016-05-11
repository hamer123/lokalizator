package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Table(name="location")
@NamedQueries(value={
		@NamedQuery(name="Location.findOlderThan", query="SELECT l FROM Location l WHERE l.date > :date"),
		@NamedQuery(name="Location.findYoungerThan", query="SELECT l FROM Location l WHERE l.date > :date"),
		@NamedQuery(name="Location.deleteById", query="DELETE FROM Location l WHERE l.id = :id"),
		@NamedQuery(name="Location.deleteOlderThan", query="DELETE FROM Location l WHERE l.user = :user AND l.date < :date"),
		@NamedQuery(name="Location.deleteYoungerThan", query="DELETE FROM Location l WHERE l.user = :user AND l.date > :date"),
		@NamedQuery(name="Location.count", query="SELECT COUNT(l) FROM Location l where l.user = :user"),
		@NamedQuery(name="Location.findOlderThanAndYoungerThan", query="SELECT l FROM Location l WHERE l.date > :younger AND l.date < :older"),
		@NamedQuery(name="Location.findGps", query="SELECT l FROM Location l WHERE l.id IN (:id)"),
		@NamedQuery(name="Location.findWhereAddressIsNull", query="SELECT l FROM Location l WHERE l.address.city is null"),
		@NamedQuery(name="Location.findByUserIdWhereYoungerThanAndOlderThanOrderByDateDesc",
		            query="SELECT l FROM Location l WHERE l.user.id = :id AND l.date > :older AND l.date < :younger"),

})
@NamedNativeQueries(value={
		@NamedNativeQuery(name="Location.Native.findGps",
						  query="SELECT date,latitude,longitude,user_id FROM location "
						  	  + "WHERE id IN (SELECT lastGpsLocation_id FROM user WHERE id IN (:id))"), 
		@NamedNativeQuery(name="Location.Native.findNetwork",
						  query="SELECT date,latitude,longitude,user_id FROM location "
						  	  + "WHERE id IN (SELECT lastNetworkLocation_id FROM user WHERE id IN (:id))"),
		@NamedNativeQuery(name="Location.Native.findOwn", 
		                  query="SELECT date,latitude,longitude,user_id FROM location "
			  	              + "WHERE id IN (SELECT lastOwnProviderLocation_id FROM user WHERE id IN (:id))"),
		@NamedNativeQuery(name="Location.Native.updateCity", 
			              query="UPDATE location SET address =:address "
				  	          + "WHERE latitude =:lat AND longitude =:lon"),
	    @NamedNativeQuery(name="Location.Native.findLastUserLocationsByLogin",
	                      query= "SELECT l.id as l_id, l.address, l.date, l.latitude, l.longitude, l.provider, u.id as u_id, u.login  FROM location as l "
                               + "INNER JOIN user as u " 
                               + "ON l.user_id = u.id "
                               + "WHERE l.id = (SELECT lastGpsLocation_id FROM user WHERE login = :login) OR l.id = (SELECT lastNetworkLocation_id FROM user WHERE login = :login) OR l.id = (SELECT lastOwnProviderLocation_id FROM user WHERE login = :login)")
})
public class Location implements Serializable{
    @TableGenerator(
            name="locationGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="LOC_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="locationGen")
	@Id
	private long id;
    
	@XmlElement
	@Column(nullable=false)
	private double latitude;
	
	@Column(nullable=false)
	@XmlElement
	private double longitude;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	@XmlElement
	private Date date;
	
	@XmlElement
	@Enumerated
	@Column(nullable=false)
	private Providers provider;
	
	@Embedded
	private Address address;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	public Location(){}
	
	public Location(Date date, double lat, double lon){
		this.date = date;
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public Location(double lat, double lon){
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Providers getProvider() {
		return provider;
	}

	public void setProvider(Providers provider) {
		this.provider = provider;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
