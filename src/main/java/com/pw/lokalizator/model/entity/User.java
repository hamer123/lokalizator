package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import com.pw.lokalizator.model.enums.Roles;
import org.omnifaces.cdi.Eager;


@Entity
@Table(name ="user")
@NamedQueries(
		value = {
		  @NamedQuery(name ="USER.findAll", 
				      query="SELECT u FROM User u"),
		  @NamedQuery(name ="USER.findByLogins", 
		              query="SELECT u FROM User u WHERE u.login IN (:logins)"),
		  @NamedQuery(name ="USER.deleteByID", 
		              query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name ="USER.findByLogin", 
		              query="SELECT u FROM User u WHERE u.login =:login"),
		  @NamedQuery(name ="USER.findByLoginAndPassword",
		              query="SELECT u FROM User u WHERE u.login = :login AND u.password =:password"),
		  @NamedQuery(name ="USER.findLoginByLoginLike",
		              query="SELECT u.login FROM User u WHERE u.login LIKE :login"),
		  @NamedQuery(name ="USER.findUserWithPolygonsByLogin", 
		              query="SELECT u FROM User u INNER JOIN FETCH u.areas WHERE u.login =:login"),
		  @NamedQuery(name ="USER.findUsersByIds",
		              query="SELECT u FROM User u WHERE u.id IN (:ids)"),
		  @NamedQuery(name ="USER.findUserFetchRolesByLoginAndPassword",
		              query="SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login =:login AND u.password =:password"),
		  @NamedQuery(name ="USER.findByEmail",
		              query="SELECT u FROM User u WHERE u.email =:email")
		})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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
	
	@OneToOne(optional = false, orphanRemoval = true, cascade = {CascadeType.ALL})
	@JoinColumn(name = "user_setting")
	private UserSetting userSetting;

	@Column(name="login", unique=true, updatable=false, nullable=false, length=16)
	private String login;

	@XmlTransient
	@Column(name="password", nullable=false, length=16)
	private String password;

	@Column(name="email", unique=true, nullable=false, length=50)
	private String email;

	@Column(name="phone")
	private String phone;

	@ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private List<Roles> roles = new ArrayList<>();
	
	@OneToOne
	private Avatar avatar;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_gps_id")
	private LocationGPS lastLocationGPS;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_network_nasz_id")
	private LocationNetwork lastLocationNetworkNaszaUsluga;
	
	@OneToOne
	@JoinColumn(updatable = true, name = "last_location_network_obcy_id")
	private LocationNetwork lastLocationNetworObcaUsluga;

	@XmlTransient
	@OneToMany(mappedBy = "provider", orphanRemoval = true, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<Area>areas;

	public User(){}
	
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

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> polygons) {
		this.areas = polygons;
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

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
}
