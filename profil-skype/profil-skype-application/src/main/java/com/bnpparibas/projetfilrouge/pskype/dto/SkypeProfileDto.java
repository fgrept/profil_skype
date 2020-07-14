package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Date;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

/**
 * Classe dédiée au SkypeProfile qui assure le lien avec la couche application
 * @author Judicaël
 *
 */
public class SkypeProfileDto {
	
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
	
	private String collaboraterId;
	private String itCorrespondantId;
	
	//statut du profil : activé, désactivé, expiré (si date d'expiration supérieure > date du jour)
	private StatusSkypeProfileEnum statusProfile;
	
	private Date expirationDate;
	
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

	public SkypeProfileDto(String SIP, String collaboraterId, String itCorrespondantId) {
		this.SIP=SIP;
		this.enterpriseVoiceEnabled=true;
		this.voicePolicy="EMEA-VP-FR_BDDF_NationalOnlyAuthorized";
		this.dialPlan="DP-FR";
		this.exchUser="Vide";
		this.exUmEnabled=false;
		this.objectClass="user";
		this.collaboraterId=collaboraterId;
		this.itCorrespondantId=itCorrespondantId;
		this.statusProfile=StatusSkypeProfileEnum.ENABLED;
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
	
}
