package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlSeeAlso({CellInfoLte.class, CellInfoGSM.class})
@XmlTransient
public abstract class CellInfoMobile implements Serializable{
    @TableGenerator(
            name="cellInfoGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="LOCATIO_NETWORK_ID"
            )
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="cellInfoGen")
    private long id;
    
	@Embedded
	private SignalStrength signalStrength;
	
	public SignalStrength getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(SignalStrength signalStrength) {
		this.signalStrength = signalStrength;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
