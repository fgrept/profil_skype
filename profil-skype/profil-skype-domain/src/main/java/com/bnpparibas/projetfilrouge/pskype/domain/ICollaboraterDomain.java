package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

/**
 * 
 * @author La fabrique
 * 
 * Interface Collaborater
 */
public interface ICollaboraterDomain {
	/**
	 * Methode create permettant de de créer un collaborater
	 * @param collaborater
	 * @return un boolean
	 */
	boolean create(Collaborater collaborater);
	
	/**
	 * Methode findByCollaboraterId permettant de réuperer un collaborater à partir de son Id annuaire
	 * @param idAnnuaire
	 * @return un objet collaborater
	 */
	Collaborater findByCollaboraterId(String idAnnuaire);
	
	/**
	 * Methode findAllCollaborater permettant de récupérer la liste de tous les collaborateurs
	 * @return la liste de tous les collaborateurs
	 */
	List<Collaborater> findAllCollaborater();
	
	/**
	 * Methode findAllCollaboraterPage permettant de gérer la pagination
	 * @param numberPage
	 * @param sizePage
	 * @param criteria
	 * @param sortAscending
	 * @return une liste de collaborateurs
	 */
	List<Collaborater> findAllCollaboraterPage(int numberPage, int sizePage, String criteria, boolean sortAscending);
	
	/**
	 * Methode findAllCollaboraterCriteriaPage permettant de récuperer une liste de collaborateur par critère de recherche
	 * @param collaborater
	 * @param numberPage
	 * @param sizePage
	 * @param attribute
	 * @param sortAscending
	 * @return
	 */
	List<Collaborater> findAllCollaboraterCriteriaPage(Collaborater collaborater, int numberPage, int sizePage,
			String attribute, boolean sortAscending);

}
