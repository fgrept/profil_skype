package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;


/**
 * Interface SkypeProfil pour la couche Domain
 * @author La Fabrique
 * 
 */
public interface ISkypeProfileDomain {
	/**
	 * Methode create de création d'un profil skype
	 * @param SkypeProfile
	 * @return un boolean
	 */
		boolean create(SkypeProfile SkypeProfile);
		
	 /**
	  * Methode de mise à jour d'un profil Skype	
	  * @param SkypeProfile
	  * @return un boolean
	  */
		boolean update(SkypeProfile SkypeProfile);
	 
		/**
		 * Méthode de suppression d'un profil skype à partir d'un SIP
		 * @param sip
		 * @return un boolean
		 */
		boolean delete(String sip);
		
		/**
		 * Méthode de consultation d'un profil Skype à partir du SIP et du Status du profil
		 * @param sip
		 * @param status
		 * @return un profil skype
		 */
		SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status);
		
		/**
		 * Méthode de recherche d'un profil skype à partir du SIP
		 * @param sip
		 * @return un profil skype
		 */
		SkypeProfile findSkypeProfileBySip(String sip);
		
		/**
		 * Méthode de recherche d'un profil skype à partir de l'Id annuaire
		 * @param idAnnuaire
		 * @return un objet profil skype
		 */
		SkypeProfile findSkypeProfileByIdCollab(String idAnnuaire);
		
		/**
		 * Méthode de recherche d'un profil skype à partir d'un objet Collaborater
		 * @param un collaborater
		 * @return un objet profil skype
		 */
		SkypeProfile findSkypeProfileByCollaborater(Collaborater collaborater);
		
		/**
		 * Méthode permettant de lister tous les profils Skype
		 * @return une liste de profil skype
		 */
		List<SkypeProfile> findAllSkypeProfile();
		
		/**
		 * Méthode permettant de lister des profils skype à partir de critère de recherche
		 * @param enterpriseVoiceEnabled
		 * @param voicePolicy
		 * @param dialPlan
		 * @param samAccountName
		 * @param exUmEnabled
		 * @param exchUser
		 * @param statusProfile
		 * @param orgaUnityCode
		 * @param siteCode
		 * @return une liste de profil skype
		 */
		List<SkypeProfile> findAllSkypeProfileFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
				String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser,
				StatusSkypeProfileEnum statusProfile, String orgaUnityCode, String siteCode);
		/**
		 * Méthode permettant de restituer le nombre de profils skype existants
		 * @return nombre de profil skype
		 */
		Long countSkypeProfile();

		/**
		 * Méthode permettant de lister tous les profils skype par pagination
		 * @param numberPage
		 * @param sizePage
		 * @param criteria
		 * @param sortAscending
		 * @return nombre de profil skype
		 */
		List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria,
				boolean sortAscending);

		/**
		 * Méthode permettant de lister une liste partielle de profil skype (gestion de pagination)
		 * @param profilDom
		 * @param numberPage
		 * @param sizePage
		 * @param sortCriteria
		 * @param sortAscending
		 * @return
		 */
		List<SkypeProfile> findAllSkypeProfileFiltersPage(SkypeProfile profilDom, int numberPage, int sizePage,
				String sortCriteria, boolean sortAscending);

	
}
