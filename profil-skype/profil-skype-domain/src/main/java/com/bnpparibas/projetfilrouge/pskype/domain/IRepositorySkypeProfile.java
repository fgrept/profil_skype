package com.bnpparibas.projetfilrouge.pskype.domain;

/**
 * 
 * @author 479680
 * 
 *
 */
public interface IRepositorySkypeProfile {

	/**
	 * Méthode permettant d'ajouter un nouveau profil Skype pour un collaborateur
	 * 
	 */
		void addSkypeProfile(SkypeProfile SkypeProfile);
		
	/**
	 * Méthode permettant de récupérer un collaborateur avec ses critères d'UO et de site d'affectation
	 * 
	 */
		SkypeProfile consultSkypeProfile(String sip);

	
}
