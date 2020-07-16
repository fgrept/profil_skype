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
		
		void delete(SkypeProfile SkypeProfile);
		
	/**
	 * Méthode permettant de récupérer un collaborateur avec ses critères d'UO et de site d'affectation
	 * 
	 */
		SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status);
		
		SkypeProfile findSkypeProfileBySip(String sip);

		List<SkypeProfile> findAllSkypeProfile();
		
		List<SkypeProfile> findSkypeProfileFilters();
	
}