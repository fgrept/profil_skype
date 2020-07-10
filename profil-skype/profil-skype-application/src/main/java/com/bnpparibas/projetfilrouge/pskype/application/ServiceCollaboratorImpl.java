package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.CollaboraterWithAffectation;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboratorManagement;

@Service
public class ServiceCollaboratorImpl implements IServiceCollaborater {
	
	@Autowired
	ICollaboratorManagement collabWithAffect;

	@Override
	public CollaboraterWithAffectation searchById(Long id) {
		
		return collabWithAffect.searchById(id);
	}

	@Override
	public List<CollaboraterWithAffectation> searchByName(String firstname, String lastname) {

		return collabWithAffect.searchByName(firstname, lastname);
	}
	

}
