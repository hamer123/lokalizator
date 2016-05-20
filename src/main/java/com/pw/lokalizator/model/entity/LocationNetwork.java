package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Where;

import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Networks;

@Entity
@DiscriminatorValue(value="NETWORK")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationNetwork extends Location implements Serializable{
	
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="CELL_INFO_NETWORK_ID")
	private CellInfoMobile cellInfoMobile;
	
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="INFO_WIFI_ID")
	private WifiInfo infoWifi;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "LOC_SERVICE_TYPE", nullable = false , updatable = false)
	private LocalizationServices localizationServices;

	public CellInfoMobile getCellInfoMobile() {
		return cellInfoMobile;
	}

	public void setCellInfoMobile(CellInfoMobile cellInfoMobile) {
		this.cellInfoMobile = cellInfoMobile;
	}

	public WifiInfo getInfoWifi() {
		return infoWifi;
	}

	public void setInfoWifi(WifiInfo infoWifi) {
		this.infoWifi = infoWifi;
	}

	public LocalizationServices getLocalizationServices() {
		return localizationServices;
	}

	public void setLocalizationServices(LocalizationServices localizationServices) {
		this.localizationServices = localizationServices;
	}

	@Override
	public String toString() {
		return "LocationNetwork [cellInfoMobile="
				+ cellInfoMobile + ", infoWifi=" + infoWifi + "]" +
				super.toString();
	}
}
