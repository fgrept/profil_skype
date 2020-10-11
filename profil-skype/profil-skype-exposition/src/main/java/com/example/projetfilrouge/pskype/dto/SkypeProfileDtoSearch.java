package com.example.projetfilrouge.pskype.dto;

import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;

import java.util.Date;

/**
 * Classe de résultats sur les Profils
 * renvoyés à l'utilisateur lors d'action de recherche
 * 
 * @author La Fabrique
 *
 */
public class SkypeProfileDtoSearch extends SkypeProfileDto {
	
	// données du profilSkype
	private Date expirationDate;
	
	// données du collaborateur attaché
	// elles sont pour l'instant calées sur les possiblités de filte
	// à voir si on ramène plus de données au front (uo, site, ...)
	private String firstName;
	private String lastName;
	private String orgaUnityCode;
	private String siteCode;
	
	public SkypeProfileDtoSearch(String sIP, String enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
								 String samAccountName, String exUmEnabled, String exchUser, String objectClass,
								 StatusSkypeProfileEnum statusProfile, Date expirationDate, String collaboraterId, String firstName,
								 String lastName, String orgaUnityCode, String siteCode) {

		super(sIP, enterpriseVoiceEnabled, voicePolicy, dialPlan,
				samAccountName, exUmEnabled, exchUser, objectClass,
				collaboraterId, statusProfile);
		this.expirationDate = expirationDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.orgaUnityCode = orgaUnityCode;
		this.siteCode = siteCode;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOrgaUnityCode() {
		return orgaUnityCode;
	}
	public void setOrgaUnityCode(String orgaUnityCode) {
		this.orgaUnityCode = orgaUnityCode;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	@Override
	public String toString() {
		return "SkypeProfileDtoSearch{" +
				"expirationDate=" + expirationDate +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", orgaUnityCode='" + orgaUnityCode + '\'' +
				", siteCode='" + siteCode + '\'' +
				'}';
	}
}
