package com.example.projetfilrouge.pskype.infrastructure.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 116453
 * assure la persistence de la classe Site
 * un site ne connait l'ensemble de ces UO
 */

@Entity
public class SiteEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long siteId;
	private String siteCode;
	private String siteName;
	private String siteAddress;
	private String sitePostalCode;
	private String siteCity;
	
	
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public String getSitePostalCode() {
		return sitePostalCode;
	}
	public void setSitePostalCode(String sitePostalCode) {
		this.sitePostalCode = sitePostalCode;
	}
	public String getSiteCity() {
		return siteCity;
	}
	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}
	
	@Override
	public String toString() {
		return "SiteEntity [siteId=" + siteId + ", siteCode=" + siteCode + ", siteName=" + siteName + ", siteAddress="
				+ siteAddress + ", sitePostalCode=" + sitePostalCode + ", siteCity=" + siteCity + "]";
	}
	
}
