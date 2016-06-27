package com.pw.lokalizator.model.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import com.pw.lokalizator.model.enums.LocalizationServices;

@Entity
@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsNaszOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
				   		 + "l.date > :older AND l.date < :younger AND "
				   		 + "l.localizationServices = com.pw.lokalizator.model.enums.LocalizationServices.NASZ "
				   		 + "ORDER BY l.date DESC"),
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanAndOlderThanAndServiceEqualsObcyOrderByDateDesc",
				   query = "SELECT l FROM LocationNetwork l WHERE l.user.login =:login AND "
						 + "l.date > :older AND l.date < :younger AND "
						 + "l.localizationServices = com.pw.lokalizator.model.enums.LocalizationServices.OBCY "
						 + "ORDER BY l.date DESC")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationNetwork extends Location implements Serializable{
	
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="CELL_INFO_NETWORK_ID")
	
	@XmlElementRefs(value = {
			@XmlElementRef(name = "cellInfoLte", type = CellInfoLte.class, required = true),
			@XmlElementRef(name = "cellInfoGSM", type = CellInfoGSM.class, required = true)
	})
	private CellInfoMobile cellInfoMobile;
	
	@OneToOne( orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="INFO_WIFI_ID")
	private WifiInfo wifiInfo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "LOC_SERVICE_TYPE", nullable = false , updatable = false)
	private LocalizationServices localizationServices;

	public CellInfoMobile getCellInfoMobile() {
		return cellInfoMobile;
	}

	public void setCellInfoMobile(CellInfoMobile cellInfoMobile) {
		this.cellInfoMobile = cellInfoMobile;
	}

	public WifiInfo getWifiInfo() {
		return wifiInfo;
	}

	public void setWifiInfo(WifiInfo infoWifi) {
		this.wifiInfo = infoWifi;
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
				+ cellInfoMobile + ", infoWifi=" + wifiInfo + "]" +
				super.toString();
	}
}
