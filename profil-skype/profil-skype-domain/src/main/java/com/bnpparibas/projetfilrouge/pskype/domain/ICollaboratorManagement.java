package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface ICollaboratorManagement {

	public Collaborater searchById (Long id);
	public List<Collaborater> searchByName (String firstname, String lastname);
	
	// TODO
	// Créer une méthode qui renvoit le collaborateur, son uo et son site selon critères
	// public List<T> searchAllById (Long id);
}
