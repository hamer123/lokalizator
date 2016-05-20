package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries(value = {
		@NamedQuery(name="WifiInfo.findByLocationId", query="SELECT l.infoWifi FROM LocationNetwork l WHERE l.id =:id")
})
public class WifiInfo implements Serializable{
	@Id
    @TableGenerator(
            name="INFO_WIFI_GEN", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="INFO_WIFI_GEN"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="INFO_WIFI_GEN")
	@Column(name="ID")
	private long id;
	@Column(name="FREQUENCY")
	private int frequency;
	@Column(name="BSSID")
	private String BSSID;
	@Column(name="IP_ADDRESS")
	private int ipAddress;
	@Column(name="LINK_SPEED")
	private int linkSpeed;
	@Column(name="MAC_ADDRESS")
	private String macAddress;
	@Column(name="NETWORK_ID")
	private int networkId;
	@Column(name="RSSI")
	private int RSSI;
	@Column(name="SSID")
	private String SSID;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getBSSID() {
		return BSSID;
	}
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}
	public int getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(int ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getLinkSpeed() {
		return linkSpeed;
	}
	public void setLinkSpeed(int linkSpeed) {
		this.linkSpeed = linkSpeed;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public int getNetworkId() {
		return networkId;
	}
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}
	public int getRSSI() {
		return RSSI;
	}
	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}
	public String getSSID() {
		return SSID;
	}
	public void setSSID(String sSID) {
		SSID = sSID;
	}
	
	@Override
	public String toString() {
		return "WifiInfo [id=" + id + ", frequency=" + frequency + ", BSSID="
				+ BSSID + ", ipAddress=" + ipAddress + ", linkSpeed="
				+ linkSpeed + ", macAddress=" + macAddress + ", networkId="
				+ networkId + ", RSSI=" + RSSI + ", SSID=" + SSID + "]";
	}
	
}
