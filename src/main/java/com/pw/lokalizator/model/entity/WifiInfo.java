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
		@NamedQuery(name="WifiInfo.findByLocationId", query="SELECT l.wifiInfo FROM LocationNetwork l WHERE l.id =:id")
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
	@Column(name="id")
	private long id;
	@Column(name="frequency")
	private int frequency;
	@Column(name="bssid")
	private String bssid;
	@Column(name="ip_address")
	private int ipAddress;
	@Column(name="link_speed")
	private int linkSpeed;
	@Column(name="mac_address")
	private String macAddress;
	@Column(name="network_id")
	private int networkId;
	@Column(name="rssi")
	private int rssi;
	@Column(name="ssid")
	private String ssid;
	
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

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
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

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
		result = prime * result + frequency;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ipAddress;
		result = prime * result + linkSpeed;
		result = prime * result
				+ ((macAddress == null) ? 0 : macAddress.hashCode());
		result = prime * result + networkId;
		result = prime * result + rssi;
		result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WifiInfo other = (WifiInfo) obj;
		if (bssid == null) {
			if (other.bssid != null)
				return false;
		} else if (!bssid.equals(other.bssid))
			return false;
		if (frequency != other.frequency)
			return false;
		if (id != other.id)
			return false;
		if (ipAddress != other.ipAddress)
			return false;
		if (linkSpeed != other.linkSpeed)
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (networkId != other.networkId)
			return false;
		if (rssi != other.rssi)
			return false;
		if (ssid == null) {
			if (other.ssid != null)
				return false;
		} else if (!ssid.equals(other.ssid))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "WifiInfo [id=" + id + ", frequency=" + frequency + ", BSSID="
				+ bssid + ", ipAddress=" + ipAddress + ", linkSpeed="
				+ linkSpeed + ", macAddress=" + macAddress + ", networkId="
				+ networkId + ", RSSI=" + rssi + ", SSID=" + ssid + "]";
	}
	
}
