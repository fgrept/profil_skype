package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

/**
 * 
 * @author 479680
 * 
 *
 */
public interface ISkypeProfileDomain {

	/**
	 * Méthode permettant d'ajouter un nouveau profil Skype pour un collaborateur
	 * 
	 */
		void create(SkypeProfile SkypeProfile);
		
		void update(SkypeProfile SkypeProfile);
		
		void delete(String sip);
		

		SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status);
		
		SkypeProfile findSkypeProfileBySip(String sip);

		List<SkypeProfile> findAllSkypeProfile();
		
		/**
		 * Méthode permettant de récupérer des profils Skype selon une liste de critères fournis en entrée
		 * 
		 * @param enterpriseVoiceEnabled
		 * @param voicePolicy
		 * @param dialPlan
		 * @param samAccountName
		 * @param exUmEnabled
		 * @param exchUser
		 * @return
		 */
		List<SkypeProfile> findAllSkypeProfileFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
				String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser,
				StatusSkypeProfileEnum statusProfile, String orgaUnityCode, String siteCode);

	
}
