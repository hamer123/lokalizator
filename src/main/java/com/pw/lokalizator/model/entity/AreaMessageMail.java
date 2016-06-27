package com.pw.lokalizator.model.entity;

import java.util.Date;

import javax.enterprise.inject.Default;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.pw.lokalizator.model.enums.AreaMailMessageModes;

@Entity
@Table(name = "area_message_mail")
public class AreaMessageMail {
    @TableGenerator
    (
     name="areaMessageMailGenerator", 
     table="ID_GEN", 
     pkColumnName="GEN_KEY", 
     valueColumnName="GEN_VALUE", 
     pkColumnValue="area_message_mail_ID"
    )
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="areaMessageMailGenerator")
    @Column(name = "id")
	private long id;
    
    @Column(name = "active")
    private boolean active;
    
    @Column(name = "accept")
    private boolean accept = true;
    
    @Column(name = "mode")
    @Enumerated(EnumType.STRING)
    private AreaMailMessageModes areaMailMessageMode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public AreaMailMessageModes getAreaMailMessageMode() {
		return areaMailMessageMode;
	}

	public void setAreaMailMessageMode(AreaMailMessageModes areaMailMessageMode) {
		this.areaMailMessageMode = areaMailMessageMode;
	}


}
