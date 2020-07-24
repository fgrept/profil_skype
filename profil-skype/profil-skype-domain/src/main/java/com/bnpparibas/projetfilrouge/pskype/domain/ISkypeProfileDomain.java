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
		
		void update(String sip,SkypeProfile SkypeProfile);
		
		void delete(String sip);
		
	/**
	 * Méthode permettant de récupérer un collaborateur avec ses critères d'UO et de site d'affectation
	 * 
	 */
		SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status);
		
		SkypeProfile findSkypeProfileBySip(String sip);
		
		SkypeProfile findSkypeProfileByCollaborater(Collaborater collaborater);
		
		List<SkypeProfile> findAllSkypeProfile();
		
		List<SkypeProfile> findSkypeProfileFilters();

		

	
}
