package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;

public interface IServiceCollaborater {
	
	public Collaborater searchById (Long id);
	public List<Collaborater> searchByName (String firstname, String lastname);
	
}
