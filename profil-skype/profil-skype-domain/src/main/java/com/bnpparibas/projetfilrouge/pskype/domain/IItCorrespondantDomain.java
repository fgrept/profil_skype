package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface IItCorrespondantDomain {
	void create(ItCorrespondant itCorrespondant);
	void update(ItCorrespondant itCorrespondant);
	void delete(ItCorrespondant itCorrespondant);
	List<ItCorrespondant> findAllItCorrespondant();
	List<ItCorrespondant> findAllItCorrespondantFilters(String id, String lastName, String firstName);
	ItCorrespondant findItCorrespondantByCollaboraterId(String id);
}
