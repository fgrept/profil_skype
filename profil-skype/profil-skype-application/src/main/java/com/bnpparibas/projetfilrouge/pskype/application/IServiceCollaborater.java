package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.CollaboraterWithAffectation;

public interface IServiceCollaborater {
	
	public CollaboraterWithAffectation searchById (Long id);
	public List<CollaboraterWithAffectation> searchByName (String firstname, String lastname);
	
}
