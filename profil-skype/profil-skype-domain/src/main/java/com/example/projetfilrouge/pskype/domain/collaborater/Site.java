package com.example.projetfilrouge.pskype.domain.collaborater;

import javax.validation.constraints.Size;

/**
 * Liste des sites physiques avec leur adresse
 * Cette classe permet de situer physiquement un collaborateur
 * @author Judicaël
 * @version V0.2
 *
 */
public class Site {
	
	// Les annotations de validation sont utilisées par le batch de chargement du référentiel
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

	public Site(String code){
		this.siteCode=code;
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

}
