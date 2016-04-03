package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;


@Entity
@NamedQueries(value = {
		/* @NamedQuery(name="Friend.finyByUserId", query="SELECT new com.pw.lokalizator.model.Friend(f.id, f.friend) FROM Friend AS f WHERE f.user.id = :id"), */
		@NamedQuery(name="Friend.finyByUserId", query="SELECT new com.pw.lokalizator.model.Friend(f.friend.id, f.friend.login, f.friend.latitude, f.friend.longitude, f.friend.date)"
				                                    + " FROM Friend AS f "
				                                    + " WHERE f.user.id = :id")
		})
public class Friend implements Serializable{
	@Id
    @TableGenerator(
            name="friendGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="FRIEND_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="friendGen")
	private long id;
	
	@ManyToOne
	private User user;
	@ManyToOne
	private User friend;
	
	public Friend() {}
	
	public Friend(long id, User friend) {
		this.id = id;
		this.friend = friend;
	}
	
	public Friend(long id, String login, double lat, double lon, Date date) {
		this.friend = new User();
		friend.setLogin(login);
		friend.setId(id);
		friend.setDate(date);
		friend.setLatitude(lat);
		friend.setLongitude(lon);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getFriend() {
		return friend;
	}
	public void setFriend(User friend) {
		this.friend = friend;
	}
}
