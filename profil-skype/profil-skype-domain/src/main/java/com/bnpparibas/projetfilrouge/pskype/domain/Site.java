package com.bnpparibas.projetfilrouge.pskype.domain;

import javax.validation.constraints.Size;

/**
 * Liste des sites physiques avec leur adresse
 * Cette classe permet de situer physiquement un collaborateur
 * @author Judicaël
 * @version V0.1
 *
 */
public class Site {
	@Size(min = 1)
	private String siteCode;
	@Size(min = 1)
	private String siteName;
	@Size(min = 1)
	private String siteAddress;
	@Size(min = 1)
	private String sitePostalCode;
	@Size(min = 1)
	private String siteCity;
	
	public Site() {
		super();
	}

	public Site(String code,String name,String address,String postalCode, String city) {
		this.siteCode=code;
		this.siteName=name;
		this.siteAddress=address;
		this.sitePostalCode=postalCode;
		this.siteCity=city;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public String getSitePostalCode() {
		return sitePostalCode;
	}

	public String getSiteCity() {
		return siteCity;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public void setSitePostalCode(String sitePostalCode) {
		this.sitePostalCode = sitePostalCode;
	}

	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}
	
}
