package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
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
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name="USER.findByLogin", query="SELECT u FROM User u WHERE u.login =:login"),
		  @NamedQuery(name="USER.findByLoginAndPassword", query="SELECT u FROM User u WHERE u.login = :login AND u.password =:password"),
		  @NamedQuery(name="USER.findLoginByLoginLike", query="SELECT u.login FROM User u WHERE u.login LIKE :login"),
		  @NamedQuery(name="USER.findUserWithPolygonsByLogin", query="SELECT u FROM User u INNER JOIN FETCH u.polygons WHERE u.login =:login"),
		  @NamedQuery(name="USER.findUsersById", query="SELECT u FROM User u WHERE u.id IN (:id)"),
		  @NamedQuery(name="USER.findUserWithSecurityByLoginAndPassword", 
		              query="SELECT new com.pw.lokalizator.model.entity.User(u.id, u.login, u.password, u.email, u.phone, u.rola) "
		  		           +"From User u WHERE u.login =:login AND u.password =:password"),
		  @NamedQuery(name="USER.findByIdFetchEagerLastLocations", 
		              query="SELECT u FROM User u LEFT JOIN FETCH u.lastLocationGPS LEFT JOIN FETCH u.lastLocationNetworkNaszaUsluga LEFT JOIN FETCH u.lastLocationNetworObcaUsluga "
		              	  + "WHERE u.login =:login")
		})
@NamedNativeQueries(value = {
		@NamedNativeQuery(name="User.Native.updateUserLastLocationNetworkNaszaUslugaById", 
				          query="UPDATE user SET LAST_LOC_NET_NASZA_ID =:locationId WHERE id =:userId")
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
	
	@XmlElement
	@Column(unique=true, updatable=false, nullable=false, length=16)
	private String login;
	
	@XmlElement
	@Column(unique=false, nullable=false, length=16)
	private String password;

	@Column(unique=true, length=50)
	private String email;

	@Column(name="PHONE")
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private Roles rola;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = true, name = "LAST_LOC_GPS")
	private LocationGPS lastLocationGPS;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = true, name = "LAST_LOC_NET_NASZA_ID")
	private LocationNetwork lastLocationNetworkNaszaUsluga;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = true, name = "LAST_LOC_NET_OBCA_ID")
	private LocationNetwork lastLocationNetworObcaUsluga;
	
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<LocationGPS> locationGPS;
	
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<LocationNetwork> locationNetworkNaszaUsluga;
	
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<LocationNetwork> locationNetworkObcaUsluga;

	@OneToMany(mappedBy = "provider", orphanRemoval = true, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<PolygonModel>polygons;
	
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

	public List<PolygonModel> getPolygons() {
		return polygons;
	}

	public void setPolygons(List<PolygonModel> polygons) {
		this.polygons = polygons;
	}

	public List<LocationGPS> getLocationGPS() {
		return locationGPS;
	}

	public void setLocationGPS(List<LocationGPS> locationGPS) {
		this.locationGPS = locationGPS;
	}

	public List<LocationNetwork> getLocationNetwork() {
		return locationNetworkNaszaUsluga;
	}

	public void setLocationNetwork(List<LocationNetwork> locationNetwork) {
		this.locationNetworkNaszaUsluga = locationNetwork;
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

	public List<LocationNetwork> getLocationNetworkNaszaUsluga() {
		return locationNetworkNaszaUsluga;
	}

	public void setLocationNetworkNaszaUsluga(
			List<LocationNetwork> locationNetworkNaszaUsluga) {
		this.locationNetworkNaszaUsluga = locationNetworkNaszaUsluga;
	}

	public List<LocationNetwork> getLocationNetworkObcaUsluga() {
		return locationNetworkObcaUsluga;
	}

	public void setLocationNetworkObcaUsluga(
			List<LocationNetwork> locationNetworkObcaUsluga) {
		this.locationNetworkObcaUsluga = locationNetworkObcaUsluga;
	}

	public Roles getRola() {
		return rola;
	}

	public void setRola(Roles rola) {
		this.rola = rola;
	}
	
}
