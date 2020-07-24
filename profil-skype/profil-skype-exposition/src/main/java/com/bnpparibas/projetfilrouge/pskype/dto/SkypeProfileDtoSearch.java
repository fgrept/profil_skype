package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Date;

import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

/**
 * Classe de résultats sur les Profils
 * renvoyés à l'utilisateur lors d'action de recherche
 * 
 * @author La Fabrique
 *
 */
public class SkypeProfileDtoSearch {
	
	// données du profilSkype
	private String SIP;
	private boolean enterpriseVoiceEnabled;	
	private String voicePolicy;
	private String dialPlan;
	private String samAccountName;
	private boolean exUmEnabled;
	private String exchUser;
	private String objectClass;
	private StatusSkypeProfileEnum statusProfile;	
	private Date expirationDate;
	
	// données du collaborateur attaché
	// elles sont pour l'instant calées sur les possiblités de filte
	// à voir si on ramène plus de données au front (uo, site, ...)
	private String collaboraterId;
	private String firstName;
	private String lastName;
	private String orgaUnityCode;
	private String siteCode;
	
	public SkypeProfileDtoSearch(String sIP, boolean enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, boolean exUmEnabled, String exchUser, String objectClass,
			StatusSkypeProfileEnum statusProfile, Date expirationDate, String collaboraterId, String firstName,
			String lastName, String orgaUnityCode, String siteCode) {
		super();
		SIP = sIP;
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
		this.voicePolicy = voicePolicy;
		this.dialPlan = dialPlan;
		this.samAccountName = samAccountName;
		this.exUmEnabled = exUmEnabled;
		this.exchUser = exchUser;
		this.objectClass = objectClass;
		this.statusProfile = statusProfile;
		this.expirationDate = expirationDate;
		this.collaboraterId = collaboraterId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.orgaUnityCode = orgaUnityCode;
		this.siteCode = siteCode;
	}
	public String getSIP() {
		return SIP;
	}
	public void setSIP(String sIP) {
		SIP = sIP;
	}
	public boolean isEnterpriseVoiceEnabled() {
		return enterpriseVoiceEnabled;
	}
	public void setEnterpriseVoiceEnabled(boolean enterpriseVoiceEnabled) {
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
	}
	public String getVoicePolicy() {
		return voicePolicy;
	}
	public void setVoicePolicy(String voicePolicy) {
		this.voicePolicy = voicePolicy;
	}
	public String getDialPlan() {
		return dialPlan;
	}
	public void setDialPlan(String dialPlan) {
		this.dialPlan = dialPlan;
	}
	public String getSamAccountName() {
		return samAccountName;
	}
	public void setSamAccountName(String samAccountName) {
		this.samAccountName = samAccountName;
	}
	public boolean isExUmEnabled() {
		return exUmEnabled;
	}
	public void setExUmEnabled(boolean exUmEnabled) {
		this.exUmEnabled = exUmEnabled;
	}
	public String getExchUser() {
		return exchUser;
	}
	public void setExchUser(String exchUser) {
		this.exchUser = exchUser;
	}
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	public StatusSkypeProfileEnum getStatusProfile() {
		return statusProfile;
	}
	public void setStatusProfile(StatusSkypeProfileEnum statusProfile) {
		this.statusProfile = statusProfile;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCollaboraterId() {
		return collaboraterId;
	}
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
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
	
}
