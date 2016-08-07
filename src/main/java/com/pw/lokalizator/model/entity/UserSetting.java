package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
public class UserSetting implements Serializable{
	@Id
    @TableGenerator(
            name="userSettingGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="USER_SETTING_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="userSettingGen")
	private long id;
	@Column(name = "default_latitude")
	private double defaultLatitude;
	@Column(name = "default_longtitude")
	private double defaultLongtitude;
	@Column(name = "g_map_zoom")
	private int gMapZoom;
	
	
	public double getDefaultLatitude() {
		return defaultLatitude;
	}
	public void setDefaultLatitude(double defaultLatitude) {
		this.defaultLatitude = defaultLatitude;
	}
	public double getDefaultLongtitude() {
		return defaultLongtitude;
	}
	public void setDefaultLongtitude(double defaultLongtitude) {
		this.defaultLongtitude = defaultLongtitude;
	}
	public int getgMapZoom() {
		return gMapZoom;
	}
	public void setgMapZoom(int gMapZoom) {
		this.gMapZoom = gMapZoom;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
