package com.pw.lokalizator.model.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlRootElement(name = "cellInfoLte")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellInfoLte extends CellInfoMobile{
	@Column(name="ci")
    private int ci;
	@Column(name="mcc")
    private int mcc;
	@Column(name="mnc")
    private int mnc;
	@Column(name="pci")
    private int pci;
	@Column(name="tac")
    private int tac;
	@Column(name="timing_advance")
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
