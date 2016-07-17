package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pw.lokalizator.model.enums.Roles;



@Entity
@Table(name ="user")
@NamedQueries(
		value = {
		  @NamedQuery(name="USER.findAll", query = "SELECT u FROM User u"),
		  @NamedQuery(name="USER.findByLogins", query = "SELECT u FROM User u WHERE u.login IN (:logins)"),
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name="USER.findByLogin", query="SELECT u FROM User u WHERE u.login =:login"),
		  @NamedQuery(name="USER.findByLoginAndPassword", query="SELECT u FROM User u WHERE u.login = :login AND u.password =:password"),
		  @NamedQuery(name="USER.findLoginByLoginLike", query="SELECT u.login FROM User u WHERE u.login LIKE :login"),
		  @NamedQuery(name="USER.findUserWithPolygonsByLogin", query="SELECT u FROM User u INNER JOIN FETCH u.areas WHERE u.login =:login"),
		  @NamedQuery(name="USER.findUsersById", query="SELECT u FROM User u WHERE u.id IN (:id)"),
		  @NamedQuery(name="USER.findUserWithSecurityByLoginAndPassword", 
		              query="SELECT new com.pw.lokalizator.model.entity.User(u.id, u.login, u.password, u.email, u.phone, u.rola) "
		  		           +"From User u WHERE u.login =:login AND u.password =:password"),
		  @NamedQuery(name="USER.findByIdFetchEagerLastLocations", 
		              query="SELECT u FROM User u LEFT JOIN FETCH u.lastLocationGPS LEFT JOIN FETCH u.lastLocationNetworkNaszaUsluga LEFT JOIN FETCH u.lastLocationNetworObcaUsluga "
		              	  + "WHERE u.login =:login")
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
	
	@Column(name = "user_setting", nullable = false)
	@Embedded
	private UserSetting userSetting;
	
	@XmlElement
	@Column(name="login", unique=true, updatable=false, nullable=false, length=16)
	private String login;
	
	@XmlElement
	@Column(name="password", unique=false, nullable=false, length=16)
	private String password;

	@Column(name="email", unique=true, length=50)
	private String email;

	@Column(name="phone")
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "rola")
	private Roles rola;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_gps_id")
	private LocationGPS lastLocationGPS;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_network_nasz_id")
	private LocationNetwork lastLocationNetworkNaszaUsluga;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_network_obcy_id")
	private LocationNetwork lastLocationNetworObcaUsluga;
	
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<LocationGPS> locationGPS;
	
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<LocationNetwork> locationNetwork;
	
	@OneToMany(mappedBy = "provider", orphanRemoval = true, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<Area>areas;
	
	public User(){}
	
	public User(long id, String login, String password, String email, String phone, Roles rola){
		this.id = id;
		this.login = login;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.rola = rola;
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

	public LocationGPS getLastLocationGPS() {
		return lastLocationGPS;
	}

	public void setLastLocationGPS(LocationGPS lastLocationGPS) {
		this.lastLocationGPS = lastLocationGPS;
	}

	public List<Area> getArea() {
		return areas;
	}

	public void setArea(List<Area> polygons) {
		this.areas = polygons;
	}

	public List<LocationGPS> getLocationGPS() {
		return locationGPS;
	}

	public void setLocationGPS(List<LocationGPS> locationGPS) {
		this.locationGPS = locationGPS;
	}

	public List<LocationNetwork> getLocationNetwork() {
		return locationNetwork;
	}

	public void setLocationNetwork(List<LocationNetwork> locationNetwork) {
		this.locationNetwork = locationNetwork;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocationNetwork getLastLocationNetworkNaszaUsluga() {
		return lastLocationNetworkNaszaUsluga;
	}

	public void setLastLocationNetworkNaszaUsluga(
			LocationNetwork lastLocationNetworkNaszaUsluga) {
		this.lastLocationNetworkNaszaUsluga = lastLocationNetworkNaszaUsluga;
	}

	public LocationNetwork getLastLocationNetworObcaUsluga() {
		return lastLocationNetworObcaUsluga;
	}

	public void setLastLocationNetworObcaUsluga(
			LocationNetwork lastLocationNetworObcaUsluga) {
		this.lastLocationNetworObcaUsluga = lastLocationNetworObcaUsluga;
	}

	public Roles getRola() {
		return rola;
	}

	public void setRola(Roles rola) {
		this.rola = rola;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

}
