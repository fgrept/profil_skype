package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Date;

import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

/**
 * Classe exposée à l'utilisateur sur le profil Skype pour toute action de création, mise à jour
 * @author La Fabrique
 *
 */
public class SkypeProfileDto {
	
	// données du profil
	private String SIP;
	private boolean enterpriseVoiceEnabled;	
	private String voicePolicy;
	private String dialPlan;
	private String samAccountName;
	private boolean exUmEnabled;
	private String exchUser;
	private String objectClass;
	//statut du profil : activé, désactivé, expiré
	private StatusSkypeProfileEnum statusProfile;	
	
	// données du collaborateur possédant ce profil skype
	@Size(max = 17)
	private String collaboraterId;
	
	//toute action sur un profil skype est réalisée par un CIL et sera tracée en base évènement
	@Size(max = 17)
	private String itCorrespondantId;
	private String eventComment;
	
	
	public StatusSkypeProfileEnum getStatusProfile() {
		return statusProfile;
	}

	public void setStatusProfile(StatusSkypeProfileEnum statusProfile) {
		this.statusProfile = statusProfile;
	}

	public SkypeProfileDto(String SIP, String collaboraterId, String itCorrespondantId) {
		this.SIP=SIP;
		this.enterpriseVoiceEnabled=true;
		this.voicePolicy="EMEA-VP-FR_BDDF_NationalOnlyAuthorized";
		this.dialPlan="DP-EN";
		this.exchUser="mehdi.el@gmail.com";
		this.exUmEnabled=false;
		this.objectClass="user";
		this.collaboraterId=collaboraterId;
		this.itCorrespondantId=itCorrespondantId;
		this.statusProfile=StatusSkypeProfileEnum.ENABLED;
	}
	
	public SkypeProfileDto(String sIP, boolean enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, boolean exUmEnabled, String exchUser, String objectClass,
			@Size(max = 17) String collaboraterId, @Size(max = 17) String itCorrespondantId,
			StatusSkypeProfileEnum statusProfile, String eventComment) {
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
		this.itCorrespondantId = itCorrespondantId;
		this.statusProfile = statusProfile;
		this.eventComment = eventComment;
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
	public String getItCorrespondantId() {
		return itCorrespondantId;
	}
	public void setItCorrespondantId(String itCorrespondantId) {
		this.itCorrespondantId = itCorrespondantId;
	}

	public String getEventComment() {
		return eventComment;
	}

	public void setEventComment(String eventComment) {
		this.eventComment = eventComment;
	}
	
}
