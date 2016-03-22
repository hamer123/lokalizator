package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
		  @NamedQuery(name="USER.deleteByID", query="DELETE FROM User u WHERE u.id = :id")
		})

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Serializable {
	@Id
	@SequenceGenerator(name="USER_SEQ",sequenceName="USER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_SEQ")
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
	@OneToOne(cascade={CascadeType.ALL})
	private UserSecurity userSecurity;
	
	@XmlElement
	@OneToOne(cascade={CascadeType.ALL})
	private CurrentLocation currentLocation;
	
	@XmlElement
	@OneToMany
	private Collection<Location>locations;
	
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
}
