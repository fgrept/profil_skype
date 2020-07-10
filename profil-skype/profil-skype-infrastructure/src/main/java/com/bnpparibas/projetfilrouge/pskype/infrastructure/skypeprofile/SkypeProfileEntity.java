package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;


/**
 * 
 * Entité du Profil Skype
 * @author 479680
 *
 */

@Entity
public class SkypeProfileEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSkypeProfile;
	
	private String SIP;
	
	private boolean enterpriseVoiceEnabled;

	private String voicePolicy;
		 
	private String dialPlan;
	
 	private String samAccountName;

 	private boolean exUmEnabled;
		 
	private String exchUser;
		 
	private String objectClass;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	private Collaborater collaborater;
	
	
	public Long getIdSkypeProfile() {
		return idSkypeProfile;
	}

	public void setIdSkypeProfile(Long idSkypeProfile) {
		this.idSkypeProfile = idSkypeProfile;
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

	public String getSkypeProfileStatus() {
		return skypeProfileStatus;
	}

	public void setSkypeProfileStatus(String skypeProfileStatus) {
		this.skypeProfileStatus = skypeProfileStatus;
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public Collaborater getCollaborater() {
		return collaborater;
	}

	public void setCollaborater(Collaborater collaborater) {
		this.collaborater = collaborater;
	}

	private String skypeProfileStatus;
	
	private Date dateExpiration;


	
		
	
}
