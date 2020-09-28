package com.example.projetfilrouge.pskype.dto;


import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * Classe exposée à l'utilisateur sur le profil Skype pour toute action de création, mise à jour
 * @author La Fabrique
 *
 */
public class SkypeProfileDto {
	
	// DONNEES DU PROFIL
	// ******************************************************************************
	//syntaxe d'un SIP (RFC 3261) : URI = sip:x@y:Port x=nom d’utilisateur et y=hôte (domaine ou IP)
	@Pattern(regexp = "^sip:.*$", message = "l'adresse skype doit commencer par sip:")
	private String SIP;
	@Pattern(regexp = "^(true|false)$", message = "enterpriseVoiceEnabled doit être true ou false")
	private String enterpriseVoiceEnabled;	
	private String voicePolicy;
	private String dialPlan;
	private String samAccountName;
	@Pattern(regexp = "^(true|false)$", message = "exUmEnabled doit être true ou false")
	private String exUmEnabled;
	private String exchUser;
	private String objectClass;
	//statut du profil : activé, désactivé, expiré
	@NotNull
	private StatusSkypeProfileEnum statusProfile;


	// DONNEES DU COLLABORATEUR POSSEDANT CE PROFIL SKYPE
	// ******************************************************************************
	@Size(min = 1, max = 17, message = "l'identifiant de l'utilisateur doit être compris entre 1 et  17 caractères")
	@NotNull
	private String collaboraterId;
	
	
	public StatusSkypeProfileEnum getStatusProfile() {
		return statusProfile;
	}

	public void setStatusProfile(StatusSkypeProfileEnum statusProfile) {
		this.statusProfile = statusProfile;
	}

	public SkypeProfileDto() {
		
	}

	
	public SkypeProfileDto(String sIP, String enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, String exUmEnabled, String exchUser, String objectClass,
			@Size(max = 17) String collaboraterId,StatusSkypeProfileEnum statusProfile) {
		super();
		SIP = sIP;
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
		this.voicePolicy = voicePolicy;
		this.dialPlan = dialPlan;
		this.samAccountName = samAccountName;
		this.exUmEnabled = exUmEnabled;
		this.exchUser = exchUser;
		this.objectClass = objectClass;
		this.collaboraterId = collaboraterId;
		this.statusProfile = statusProfile;
	}

	public String getEnterpriseVoiceEnabled() {
		return enterpriseVoiceEnabled;
	}
	public void setEnterpriseVoiceEnabled(String enterpriseVoiceEnabled) {
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
	public String getExUmEnabled() {
		return exUmEnabled;
	}
	public void setExUmEnabled(String exUmEnabled) {
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
	public String getSIP() {
		return SIP;
	}
	public void setSIP(String sIP) {
		SIP = sIP;
	}
	
	public String getCollaboraterId() {
		return collaboraterId;
	}
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
	}
}
