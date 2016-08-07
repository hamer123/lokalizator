package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.pw.lokalizator.model.enums.ImageTypes;

@Entity
public class Avatar implements Serializable{
	@Id
    @TableGenerator(
            name="avatarGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="AVATAR_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="avatarGen")
	private long id;
	
	private ImageTypes format;
	private String uuid;
	private String name;
	private long size;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public ImageTypes getFormat() {
		return format;
	}
	public void setFormat(ImageTypes format) {
		this.format = format;
	}

}
