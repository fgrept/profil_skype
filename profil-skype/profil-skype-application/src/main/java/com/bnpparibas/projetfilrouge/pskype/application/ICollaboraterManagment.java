package com.bnpparibas.projetfilrouge.pskype.application;
import java.util.List;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;;

/**
 * 
 * Liste des méthodes Collaborater disponibles pour la couche exposition
 * @author Judicaël
 *
 */

public interface ICollaboraterManagment {
	/**
	 * Methode create permettant de de créer un collaborater
	 * @param collaborater
	 * @return un boolean
	 */
	boolean createCollaborater(Collaborater collaborater);
	
	/**
	 * Methode findByCollaboraterId permettant de réuperer un collaborater à partir de son Id annuaire
	 * @param idAnnuaire
	 * @return un objet collaborater
	 */
	Collaborater findCollaboraterbyIdAnnuaire(String idAnnuaire);
	
	
	/**
	 * Methode findAllCollaborater permettant de récupérer la liste de tous les collaborateurs
	 * @return la liste de tous les collaborateurs
	 */
	List<Collaborater> listCollaborater();
	
	/**
	 * Methode findAllCollaboraterPage permettant de gérer la pagination
	 * @param numberPage
	 * @param sizePage
	 * @param criteria
	 * @param sortAscending
	 * @return une liste de collaborateurs
	 */
	List<Collaborater> listCollaboraterSortByPage(int numberPage, int sizePage, String attribute,boolean sortAscending);
	
	/**
	 * Methode findAllCollaboraterCriteriaPage permettant de récuperer une liste de collaborateur par critère de recherche
	 * @param collaborater
	 * @param numberPage
	 * @param sizePage
	 * @param attribute
	 * @param sortAscending
	 * @return
	 */
	List<Collaborater> listCollaboraterCriteriaSortByPage(Collaborater mapperDtoToDomain, int numberPage, int sizePage,
			String criteria, boolean b);	
}
