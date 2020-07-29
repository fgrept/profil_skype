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

	boolean createCollaborater(Collaborater collaborater);
	Collaborater findCollaboraterbyIdAnnuaire(String idAnnuaire);
	List<Collaborater> listCollaborater();
	List<Collaborater> listCollaboraterSortByPage(int numberPage, int sizePage, String attribute,boolean sortAscending);
	List<Collaborater> listCollaboraterCriteriaSortByPage(Collaborater mapperDtoToDomain, int numberPage, int sizePage,
			String criteria, boolean b);	
}
