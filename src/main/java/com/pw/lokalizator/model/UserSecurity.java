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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries(
		value={
				//@NamedQuery(name="findByTokenAndService", query="SELECT s FROM UserSecurity s WHERE s.serviceKey = :skey AND s.tokenKey = :tkey")
		}
		)
@Table(name="usersecurity")
public class UserSecurity implements Serializable{
	@Id
    @TableGenerator(
            name="userSecGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="USER_SEC_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="userSecGen")
	private long id;
	@XmlElement
	private String serviceKey;
	@OneToOne
	private User user;
	@Enumerated(EnumType.STRING)
	private Roles rola;
	
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Roles getRola() {
		return rola;
	}
	public void setRola(Roles rola) {
		this.rola = rola;
	}
	
}
