package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface IItCorrespondantDomain {
	void create(ItCorrespondant itCorrespondant);
	void update(ItCorrespondant itCorrespondant);
	List<ItCorrespondant> findAllItCorrespondant();
	ItCorrespondant findItCorrespondantByCollaboraterId(String id);
}
