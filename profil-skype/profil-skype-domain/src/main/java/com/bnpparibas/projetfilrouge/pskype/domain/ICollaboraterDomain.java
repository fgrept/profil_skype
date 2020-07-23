package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface ICollaboraterDomain {
	boolean create(Collaborater collaborater);
	Collaborater findByCollaboraterId(String idAnnuaire);
	List<Collaborater> findAllCollaborater();
}
