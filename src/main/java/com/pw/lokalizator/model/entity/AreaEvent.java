package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

@MappedSuperclass
public abstract class AreaEvent implements Serializable{
    @TableGenerator
    (
     name="areaEventGenerator", 
     table="ID_GEN", 
     pkColumnName="GEN_KEY", 
     valueColumnName="GEN_VALUE", 
     pkColumnValue="AREA_EVENT_ID"
    )
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "areaEventGenerator")
	@Id
	private long id;
    
    @ManyToOne
    private Area area;
    
    private String message;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    public abstract Location getLocation();
}
