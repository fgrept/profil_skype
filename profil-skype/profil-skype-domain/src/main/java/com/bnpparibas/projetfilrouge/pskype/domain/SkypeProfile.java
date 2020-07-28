package com.bnpparibas.projetfilrouge.pskype.domain;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.Profile;

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

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public void setExpirationDateWhenReCreated() {
		this.expirationDate = calcDateExpiration();
	}


	public void setExpirationDate() {
		this.expirationDate = calcDateExpiration();
	}
	
	public SkypeProfile() {
		this.expirationDate = calcDateExpiration();
	}
	
	public SkypeProfile (String SIP, Collaborater collaborater) {
		this.SIP=SIP;
		this.collaborater=collaborater;
	    this.expirationDate = calcDateExpiration();
	}

	public SkypeProfile(String sIP, boolean enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, boolean exUmEnabled, String exchUser, String objectClass, Collaborater collaborater) {
		super();
		SIP = sIP;
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
		this.voicePolicy = voicePolicy;
		this.dialPlan = dialPlan;
		this.samAccountName = samAccountName;
		this.exUmEnabled = exUmEnabled;
		this.exchUser = exchUser;
		this.objectClass = objectClass;
		this.collaborater = collaborater;
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
	
	/**
	 * Méthode permettant de détecter les différences entre deux profils skype
	 * sur tous les champs de classe
	 * @param avant
	 * @param après
	 * @return
	 * @throws IllegalAccessException
	 */
	
	public static List<String> difference(SkypeProfile avant, SkypeProfile après) throws IllegalAccessException {
	     List<String> changedProperties = new ArrayList<>();
	     for (Field field : avant.getClass().getDeclaredFields()) {
	        // You might want to set modifier to public first (if it is not public yet)
	        field.setAccessible(true);
	        Object value1 = field.get(avant);
	        Object value2 = field.get(après); 
	        if ( (value1 != null && value2 != null) 
	        		&& (field.getName() != "SIP")
	        		&& (field.getName() != "collaborater")) {
	            System.out.println(field.getName() + "=" + value1);
	            System.out.println(field.getName() + "=" + value2);
	            if (!Objects.equals(value1, value2)) {
	                changedProperties.add(field.getName()+ " : " + value2);
	            }
	        }
	    }
	    return changedProperties;
	}
	
	@Override
	public String toString() {
		return "SkypeProfile [SIP=" + SIP + ", enterpriseVoiceEnabled=" + enterpriseVoiceEnabled + ", voicePolicy="
				+ voicePolicy + ", dialPlan=" + dialPlan + ", samAccountName=" + samAccountName + ", exUmEnabled="
				+ exUmEnabled + ", exchUser=" + exchUser + ", objectClass=" + objectClass + ", collaborater="
				+ collaborater + ", statusProfile=" + statusProfile + ", expirationDate=" + expirationDate + "]";
	}

	private Date calcDate () {
		// La date d'expiration du profil skype est de 2 ans à partir de sa date de création 
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	    return cal.getTime();
	}

}
