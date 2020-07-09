package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboratorManagement;

@Repository
public class RepositoryCollaboraterImpl implements ICollaboratorManagement {
	
	@Autowired
	IRepositoryCollaborater dao;
	
	@Autowired
	CollaboraterEntityMapper mapper;

	@Override
	public Collaborater searchById(Long id) {
		
		CollaboraterEntity entity = dao.findByCollaboraterId(id);
		
		return mapper.mapToDomain(entity);
	}

	@Override
	public List<Collaborater> searchByName(String firstname, String lastname) {
			
		List<CollaboraterEntity> entityList = dao.findByFirstNameAndLastName(firstname, lastname);
		
		return mapper.mapToDomainList(entityList);
	}

	
	
}
