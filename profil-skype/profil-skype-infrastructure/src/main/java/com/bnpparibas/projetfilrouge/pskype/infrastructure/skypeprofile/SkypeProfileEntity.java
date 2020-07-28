package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;




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
	@Enumerated(EnumType.STRING)
	private StatusSkypeProfileEnum statusProfile;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;
	

	@OneToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.PERSIST)
	private CollaboraterEntity collaborater;
	
	public SkypeProfileEntity() {
		
		this.enterpriseVoiceEnabled=false;
		this.exUmEnabled=false;
		this.statusProfile = StatusSkypeProfileEnum.ENABLED;
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

	public CollaboraterEntity getCollaborater() {
		return collaborater;
	}

	public void setCollaborater(CollaboraterEntity collaborater) {
		this.collaborater = collaborater;
	}

		
	
}
