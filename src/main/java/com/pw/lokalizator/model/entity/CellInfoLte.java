package com.pw.lokalizator.model.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CellInfoLte extends CellInfoMobile{
	@Column(name="CI")
    private int ci;
	@Column(name="MCC")
    private int mcc;
	@Column(name="MNC")
    private int mnc;
	@Column(name="PCI")
    private int pci;
	@Column(name="TAC")
    private int tac;
	@Column(name="TIMING_ADVANCE")
	private int timingAdvance;
    
	public int getCi() {
		return ci;
	}
	public void setCi(int ci) {
		this.ci = ci;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	public int getMnc() {
		return mnc;
	}
	public void setMnc(int mnc) {
		this.mnc = mnc;
	}
	public int getPci() {
		return pci;
	}
	public void setPci(int pci) {
		this.pci = pci;
	}
	public int getTac() {
		return tac;
	}
	public void setTac(int tac) {
		this.tac = tac;
	}
	public int getTimingAdvance() {
		return timingAdvance;
	}
	public void setTimingAdvance(int timingAdvance) {
		this.timingAdvance = timingAdvance;
	}
}
