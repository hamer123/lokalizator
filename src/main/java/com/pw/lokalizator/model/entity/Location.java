package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.pw.lokalizator.model.enums.Providers;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Location implements Serializable{
	@XmlElement
	@Id
	@TableGenerator(
			name="LOCATION_GEN",
			table="ID_GEN",
			pkColumnName="GEN_KEY",
			valueColumnName="GEN_VALUE",
			pkColumnValue="LOCATIO_NETWORK_ID"
	)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="LOCATION_GEN")
	@Column(name = "location_id")
    private Long id;
	
	@XmlElement
	@Column(name="latitude", nullable=false, updatable = false)
	private double latitude;

	@XmlElement
	@Column(name="longtitude", nullable=false, updatable = false)
	private double longitude;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@XmlElement
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date", nullable=false, updatable = false)
	private Date date;
	
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name="provider_type", nullable=false, updatable = false)
	private Providers providerType;
	
	@XmlElement
	@Embedded
	private Address address;
	
	@XmlElement
	@Column(name = "accuracy", nullable = false, updatable = false)
	private double accuracy;
	
	@Column(name = "event_check")
	private boolean eventCheck;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	
