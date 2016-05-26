package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Address implements Serializable{
	
	@Column(name="CITY")
	private String city;
	@Column(name="STREET")
	private String street;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	@Override
	public String toString() {
		return "Address [city=" + city + ", street=" + street + "]";
	}

}
