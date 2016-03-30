package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name ="USERS")
@NamedQueries(
		value = {
		  @NamedQuery(name="USER.findAll", query = "SELECT u FROM User u"),
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name="USER.findByLoginAndPassword", query="SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
		})

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Serializable {
	@Id
    @TableGenerator(
            name="userGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="USER_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="userGen")
	private long id;
	
	@Column(unique=true, updatable=false, nullable=false, length=16)
	@XmlElement
	private String login;
	
	@Column(unique=false, nullable=false, length=16)
	@XmlElement
	private String password;

	@Column(unique=true, length=50)
	@XmlElement
	private String email;

	@Column(nullable=true)
	@XmlElement
	private boolean enable;
	
	@XmlElement
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="user")
	private UserSecurity userSecurity;
	
	@XmlElement
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="user")
	private CurrentLocation currentLocation;
	
	@XmlElement
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private Collection<Location>locations = new ArrayList<Location>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="provider")
	private List<Polygon>polygons;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="user")
	private List<Friend>friends;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="from")
	private List<FriendInvitation>friendInvitations;
	
	public void addLocation(Location location){
		locations.add(location);
		location.setUser(this);
	}
	
	public void removeLocation(Location location){
		locations.remove(location);
		location.setUser(null);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public UserSecurity getUserSecurity() {
		return userSecurity;
	}
	public void setUserSecurity(UserSecurity userSecurity) {
		this.userSecurity = userSecurity;
	}
	public CurrentLocation getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(CurrentLocation currentLocation) {
		this.currentLocation = currentLocation;
	}
	public Collection<Location> getLocations() {
		return locations;
	}
	public void setLocations(Collection<Location> locations) {
		this.locations = locations;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(List<Polygon> polygons) {
		this.polygons = polygons;
	}
}
