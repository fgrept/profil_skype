package com.bnpparibas.projetfilrouge.pskype.domain;


import java.util.Calendar;
import java.util.Date;

/**
 * Cette classe contient les informations d'un profil Skype.
 * Il n'existe au plus qu'un seul profil skype par collaborateur
 * @author Judicaël
 * @version V0.1
 *
 */
public class SkypeProfile {
	//SIP : adresse mail skype. Ex : judicael.tige@live.bnpparibas.com
	private String SIP;
	
	private boolean enterpriseVoiceEnabled;
	
	//si voice enabled à true, peut prendre les valeurs :
	//"EMEA-VP-FR_BDDF_InternationalAuthorized" ou "EMEA-VP-FR_BDDF_NationalOnlyAuthorized"
	private String voicePolicy;
	
	//par défaut vaut DP-FR
	private String dialPlan;
	
	//n° de compte Sam. Ex : M000050137
	private String samAccountName;
	private boolean exUmEnabled;
	
	//prend les valeurs "Linked Mailbox" ou "Vide"
	private String exchUser;
	
	//par défaut vaut "user"
	private String objectClass;
	
//	@NotNull
	private Collaborater collaborater;
	
	//statut du profil : activé, désactivé, expiré (si date d'expiration supérieure > date du jour)
	private StatusSkypeProfileEnum statusProfile;
	
	private Date expirationDate;
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	
	
	public StatusSkypeProfileEnum getStatusProfile() {
		return statusProfile;
	}



	public void setStatusProfile(StatusSkypeProfileEnum statusProfile) {
		this.statusProfile = statusProfile;
	}


	public void setExpirationDate() {
		this.expirationDate = calcDateExpiration();
	}
	
	public void setExpirationDateToToday() {
		this.expirationDate = calcDate();
	}
	
	public SkypeProfile() {
		this.expirationDate = calcDateExpiration();
	}
	
	public SkypeProfile (String SIP, Collaborater collaborater) {
		this.SIP=SIP;
		this.collaborater=collaborater;
	    this.expirationDate = calcDateExpiration();
	}

	public String getSIP() {
		return SIP;
	}

	/**
	 * 
	 * @param sIP : il s'agit d'une adresse email.
	 * L'adresse mail fera l'objet d'un contrôle en respect de la RFC822
	 */
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

	public Collaborater getCollaborater() {
		return collaborater;
	}

	public void setCollaborater(Collaborater collaborater) {
		this.collaborater = collaborater;
	}
	
	private Date calcDateExpiration () {
		// La date d'expiration du profil skype est de 2 ans à partir de sa date de création 
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	    cal.add(Calendar.YEAR,2);
	    return cal.getTime();
	}
	
	private Date calcDate () {
		// La date d'expiration du profil skype est de 2 ans à partir de sa date de création 
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	    return cal.getTime();
	}
}
