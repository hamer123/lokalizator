package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@NamedQueries(value={
		@NamedQuery(name="Location.findOlderThan", query="SELECT l FROM Location l WHERE l.date > :date"),
		@NamedQuery(name="Location.deleteById", query="DELETE FROM Location l WHERE l.id = :id"),
		@NamedQuery(name="Location.deleteOlderThan", query="DELETE FROM Location l WHERE l.user = :user AND l.date < :date"),
		@NamedQuery(name="Location.deleteYoungerThan", query="DELETE FROM Location l WHERE l.user = :user AND l.date > :date"),
		@NamedQuery(name="Location.count", query="SELECT COUNT(l) FROM Location l where l.user = :user")
})
public class Location implements Serializable{
	@SequenceGenerator(allocationSize=1, initialValue=1, name="LOC_SEQ",sequenceName="LOCATION_S")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOC_SEQ")
	@Id
	private long id;
	@XmlElement
	@Column(updatable=false,nullable=false)
	private double latitude;
	@Column(updatable=false,nullable=false)
	@XmlElement
	private double longitude;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false,nullable=false)
	@XmlElement
	private Date date;
	@ManyToOne
	private User user;
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
}
