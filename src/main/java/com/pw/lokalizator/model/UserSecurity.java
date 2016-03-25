package com.pw.lokalizator.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries(
		value={
				@NamedQuery(name="findByTokenAndService", query="SELECT s FROM UserSecurity s WHERE s.serviceKey = :skey AND s.tokenKey = :tkey")
		}
		)
public class UserSecurity implements Serializable{
	@Id
	@SequenceGenerator(name="USER_SECURITY_S", sequenceName="USER_SECUITY_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_SECURITY_S")
	private long id;
	@XmlElement
	private String serviceKey;
	@XmlElement
	private String tokenKey;
	@OneToOne
	private User user;
	@Enumerated(EnumType.ORDINAL)
	private Role rola;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public String getTokenKey() {
		return tokenKey;
	}
	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Role getRola() {
		return rola;
	}
	public void setRola(Role rola) {
		this.rola = rola;
	}
	
}
