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
@Table(name ="USERS")
@NamedQueries(
		value = {
		  @NamedQuery(name="USER.findAll", query = "SELECT u FROM User u"),
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id"),
		  @NamedQuery(name="USER.findByLoginAndPassword", query="SELECT u FROM User u WHERE u.login =:login AND u.password =:password"),
		  /*query="SELECT new com.pw.lokalizator.model.User(u.id, u.login, u.password, u.email, u.enable, u.userSecurity.id,  u.userSecurity.serviceKey, u.userSecurity.rola) "
		  		         + "FROM User u WHERE u.login = :login AND u.password = :password"),*/
		  @NamedQuery(name="USER.findByLogin", query="SELECT u FROM User u WHERE u.login = :login")
		})
@NamedNativeQueries(value = {

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
	
	/*
	 * Feach default EAGER
	 */
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="user",  orphanRemoval = true)
	private UserSecurity userSecurity;
	
	@OneToOne
	private Location lastGpsLocation;
	
	@OneToOne
	private Location lastNetworkLocation;
	
	@OneToOne
	private Location lastOwnProviderLocation;
	
	/*
	 * Feach default LAZY
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Collection<Location>locations = new ArrayList<Location>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", orphanRemoval = true)
	private List<Polygon>polygons;
	
	@ManyToMany
    @JoinTable(name="FRIENDS")
	private List<User>friends;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="from", orphanRemoval = true)
	private List<FriendInvitation>friendInvitations;

	public User(){}
	
	public User(long id, String login, String password, String email, boolean enable,
			    long secId, String serviceKey, Role rola){
		this.id = id;
		this.login = login;
		this.password = password;
		this.email = email;
		this.enable = enable;
		this.userSecurity = new UserSecurity();
		this.userSecurity.setId(secId);
		this.userSecurity.setServiceKey(serviceKey);
		this.userSecurity.setRola(rola);
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

	public List<FriendInvitation> getFriendInvitations() {
		return friendInvitations;
	}

	public void setFriendInvitations(List<FriendInvitation> friendInvitations) {
		this.friendInvitations = friendInvitations;
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

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
}
