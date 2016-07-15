package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@Embeddable
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SignalStrength implements Serializable{

	@XmlElement
	@Column(name="assus_level", nullable = true)
	private int assusLevel;
	@XmlElement
	@Column(name="dbm", nullable = true)
	private int dbm;
	@XmlElement
	@Column(name="signal_level", nullable = true)
	private int level;
	
	public int getAssusLevel() {
		return assusLevel;
	}
	public void setAssusLevel(int assusLevel) {
		this.assusLevel = assusLevel;
	}
	public int getDbm() {
		return dbm;
	}
	public void setDbm(int dbm) {
		this.dbm = dbm;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

}
