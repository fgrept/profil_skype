package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface ICollaboraterDomain {
	boolean create(Collaborater collaborater);
	Collaborater findByCollaboraterId(String idAnnuaire);
	List<Collaborater> findAllCollaborater();
	List<Collaborater> findAllCollaboraterPage(int numberPage, int sizePage, String criteria, boolean sortAscending);
	List<Collaborater> findAllCollaboraterCriteriaPage(Collaborater collaborater, int numberPage, int sizePage,
			String attribute, boolean sortAscending);

}
