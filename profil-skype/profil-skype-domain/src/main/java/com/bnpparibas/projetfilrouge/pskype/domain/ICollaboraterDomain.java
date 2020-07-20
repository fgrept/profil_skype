package com.bnpparibas.projetfilrouge.pskype.domain;

public interface ICollaboraterDomain {
	void create(Collaborater collaborater);
	Collaborater findByCollaboraterId(String idAnnuaire);
	
}
