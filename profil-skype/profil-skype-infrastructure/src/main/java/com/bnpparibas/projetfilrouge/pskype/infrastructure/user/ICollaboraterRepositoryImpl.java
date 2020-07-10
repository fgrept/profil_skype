package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;

/**
 * Dédié au Collaborateur
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - Création d'un collaborateur 
 * @author Judicaël
 *
 */

@Repository
public class ICollaboraterRepositoryImpl implements ICollaboraterDomain {

@Autowired
private ICollaboraterRepository collaboraterRepository;
	
@Autowired
private CollaboraterEntityMapper collaboraterMapper;

/**
 * création d'un collaborateur en base de données
 * @param collaborateur
 */
	@Override
	public void create(Collaborater collaborater) {
		
		CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(collaborater.getCollaboraterId());
		if (collaboraterEntity == null) {
			collaboraterEntity = collaboraterMapper.mapToEntity(collaborater);
			collaboraterRepository.save(collaboraterEntity);
		}
		else {
			throw new RuntimeException("L'utilisateur "+collaborater.getCollaboraterId()+" existe déjà");
		}
	}
}
