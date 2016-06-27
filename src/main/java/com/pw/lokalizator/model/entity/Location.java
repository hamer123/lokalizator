package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pw.lokalizator.model.enums.Providers;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@MappedSuperclass
//@DiscriminatorColumn(name="PROVIDER_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Location implements Serializable{
    @Id
    @TableGenerator(
  		  name="LOCATION_GEN", 
  		  table="ID_GEN", 
  		  pkColumnName="GEN_KEY", 
  		  valueColumnName="GEN_VALUE", 
  		  pkColumnValue="LOCATIO_NETWORK_ID"
  		  )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="LOCATION_GEN")
    @Column(name="ID")
    private long id;
	
	@XmlElement
	@Column(name="LATITUDE", nullable=false, updatable = false)
	private double latitude;
	
	@Column(name="LONGTITUDE", nullable=false, updatable = false)
	@XmlElement
	private double longitude;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE", nullable=false, updatable = false)
	@XmlElement
	private Date date;
	
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name="PROVIDER_TYPE", nullable=false, updatable = false)
	private Providers providerType;
	
	@XmlElement
	@Embedded
	private Address address;
	
	@XmlElement
	@Column(name = "ACCURACY", nullable = false, updatable = false)
	private double accuracy;
	
	@Column(name = "event_check")
	private boolean eventCheck;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Providers getProviderType() {
		return providerType;
	}

	public void setProviderType(Providers providerType) {
		this.providerType = providerType;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isEventCheck() {
		return eventCheck;
	}

	public void setEventCheck(boolean eventCheck) {
		this.eventCheck = eventCheck;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", latitude=" + latitude + ", longitude="
				+ longitude + ", user=" + user + ", date=" + date
				+ ", providerType=" + providerType + ", address=" + address
				+ "]";
	}

}

	
