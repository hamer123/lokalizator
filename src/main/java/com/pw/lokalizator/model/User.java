package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name ="user")
@NamedQueries(
		value = {
		  @NamedQuery(name="USER.findAll", query = "SELECT u FROM User u"),
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name="USER.findByLoginAndPassword", query="SELECT u FROM User u WHERE u.login =:login AND u.password =:password"),
		  @NamedQuery(name="USER.findByLogin", query="SELECT u FROM User u WHERE u.login = :login"),
		  @NamedQuery(name="USER.findByLoginLike", query="SELECT new com.pw.lokalizator.model.User(u.id, u.login) FROM User u WHERE u.login LIKE :loginLike"),

	      @NamedQuery(name="USER.findByIdsGetIdAndLoginAndCurrentLocationsForAllProviders", 
	                  query="SELECT new com.pw.lokalizator.model.User(u.id, u.login) "
	                       + "FROM User u "
	                	   + "INNER JOIN u.lastGpsLocation lg "
	                       + "INNER JOIN u.lastNetworkLocation ln "
	                	   + "INNER JOIN u.lastOwnProviderLocation lo "
	                       + "WHERE u.id = :id"),
	                   
		})

@NamedNativeQueries(value = {
		@NamedNativeQuery(name="USER.Native.findUserByLoginAndPassword",
				          query="SELECT user.id, user.login, user.enable, usersecurity.rola, usersecurity.servicekey "
				          	  + "FROM user INNER JOIN usersecurity "
				          	  + "ON user.id = usersecurity.user_id "
				        	  + "WHERE user.login =:login AND user.password =:password"),
	    @NamedNativeQuery(name="USER.Native.updateGpsLocation",
	                      query="UPDATE user SET lastGpsLocation_id = :id"),
	    @NamedNativeQuery(name="User.Native.createFriend",
	                      query="INSERT INTO friends VALUES (:user_id, :friends_id)")
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

	@Column(nullable=false)
	private boolean enable;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="user",  orphanRemoval = true)
	private UserSecurity userSecurity;
	
	@OneToOne
	private Location lastGpsLocation;
	
	@OneToOne
	private Location lastNetworkLocation;
	
	@OneToOne
	private Location lastOwnProviderLocation;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<Location>locations;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", orphanRemoval = true)
	private List<PolygonModel>polygons;
	
	public User(long id, String login, boolean enable, Roles rola, String serviceKey){
		this.id = id;
		this.login = login;
		this.enable = enable;
		this.userSecurity = new UserSecurity();
		this.userSecurity.setRola(rola);
		this.userSecurity.setServiceKey(serviceKey);
	}
	
	public User(long id, String login){
		this.id = id;
		this.login = login;
	}
	
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
	public List<Location> getLocations() {
		return locations;
	}

	public List<PolygonModel> getPolygons() {
		return polygons;
	}

	public void setPolygons(List<PolygonModel> polygons) {
		this.polygons = polygons;
	}

	public Location getLastGpsLocation() {
		return lastGpsLocation;
	}

	public void setLastGpsLocation(Location lastGpsLocation) {
		this.lastGpsLocation = lastGpsLocation;
	}

	public Location getLastNetworkLocation() {
		return lastNetworkLocation;
	}

	public void setLastNetworkLocation(Location lastNetworkLocation) {
		this.lastNetworkLocation = lastNetworkLocation;
	}

	public Location getLastOwnProviderLocation() {
		return lastOwnProviderLocation;
	}

	public void setLastOwnProviderLocation(Location lastOwnProviderLocation) {
		this.lastOwnProviderLocation = lastOwnProviderLocation;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password
				+ ", email=" + email + ", enable=" + enable + ", userSecurity="
				+ userSecurity + ", lastGpsLocation=" + lastGpsLocation
				+ ", lastNetworkLocation=" + lastNetworkLocation
				+ ", lastOwnProviderLocation=" + lastOwnProviderLocation
				+ ", locations=" + locations + ", polygons=" + polygons
			    + "]";
	}
	
	
}
