package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

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
