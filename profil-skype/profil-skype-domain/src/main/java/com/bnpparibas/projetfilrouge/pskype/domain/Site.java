package com.bnpparibas.projetfilrouge.pskype.domain;

/**
 * Liste des sites physiques avec leur adresse
 * Cette classe permet de situer physiquement un collaborateur
 * @author Judicaël
 * @version V0.1
 *
 */
public class Site {
	private String siteCode;
	private String siteName;
	private String siteAddress;
	private String sitePostalCode;
	private String siteCity;
	
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
	
}
