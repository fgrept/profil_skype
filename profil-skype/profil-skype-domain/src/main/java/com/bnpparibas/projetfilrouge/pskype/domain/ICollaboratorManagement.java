package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

/**
 * 
 * @author 116453
 * 
 *
 */
public interface ICollaboratorManagement {
	/**
	 * Méthode permettant de récupérer un collaborateur avec ses critères d'UO et de site d'affectation
	 * 
	 * @param id
	 * @return
	 */
	public CollaboraterWithAffectation searchById (Long id);
	
	/**
	 * Méthode permettant de récupérer une liste de collaborateur
	 * avec leurs critères d'UO et de site d'affectation
	 * 
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	public List<CollaboraterWithAffectation> searchByName (String firstname, String lastname);
	
}
