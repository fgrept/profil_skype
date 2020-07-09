package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboratorManagement;

@Service
public class ServiceCollaboratorImpl implements IServiceCollaborater {
	
	@Autowired
	ICollaboratorManagement collab;
	
	@Override
	public Collaborater searchById(Long id) {
		
		return collab.searchById(id);
	}

	@Override
	public List<Collaborater> searchByName(String firstname, String lastname) {
	
		return collab.searchByName(firstname, lastname);
	}

}
