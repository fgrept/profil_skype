package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;

/**
 * Dédié au collaborateur uniquement
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - create
 * @author Judicaël
 *
 */
@Repository
public class CollaboraterRepositoryImpl implements ICollaboraterDomain {

	@Autowired
	CollaboraterEntityMapper mapperCollab;
	
	@Autowired
	ICollaboraterRepository collaboraterRepository;
	
	@Override
	public void create(Collaborater collaborater) {
		
		collaboraterRepository.save(mapperCollab.mapToEntity(collaborater));
	}

}
