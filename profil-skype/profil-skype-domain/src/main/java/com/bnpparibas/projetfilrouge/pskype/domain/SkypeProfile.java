package com.bnpparibas.projetfilrouge.pskype.domain;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;

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
	private String skypeProfileStatus;
	@NotNull
	private Collaborater collaborater;
	
	public SkypeProfile (String SIP, Collaborater collaborater) {
		this.SIP=SIP;
		this.collaborater=collaborater;
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

	private static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
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

	public String getSkypeProfileStatus() {
		return skypeProfileStatus;
	}

	public void setSkypeProfileStatus(String skypeProfileStatus) {
		this.skypeProfileStatus = skypeProfileStatus;
	}

	public Collaborater getCollaborater() {
		return collaborater;
	}

	public void setCollaborater(Collaborater collaborater) {
		this.collaborater = collaborater;
	}
	
}
