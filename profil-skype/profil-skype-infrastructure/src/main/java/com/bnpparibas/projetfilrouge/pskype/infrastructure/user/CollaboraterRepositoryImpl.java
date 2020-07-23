package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;

/**
 * Dédié au collaborateur uniquement
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - creation du collaborateur
 * - récupération du collaborateur à partir de l'id annuaire
 * @author Judicaël
 *
 */
@Repository
public class CollaboraterRepositoryImpl implements ICollaboraterDomain {

	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantRepositoryImpl.class);
	
	@Autowired
	CollaboraterEntityMapper mapperCollab;
	
	@Autowired
	ICollaboraterRepository collaboraterRepository;
	
	@Override
	public boolean create(Collaborater collaborater) {
		
	//	System.out.println(collaborater.toString());
		
		CollaboraterEntity entity = collaboraterRepository.save(mapperCollab.mapToEntity(collaborater));
		if (entity !=null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Collaborater findByCollaboraterId(String idAnnuaire) {
		// TODO Auto-generated method stub
		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (entity == null) {
			logger.info("Pas de collaborateur pour id :"+logger);
			return null;
		}else {
			return mapperCollab.mapToDomain(entity);
		}
	}

	@Override
	public List<Collaborater> findAllCollaborater() {
		
		List<Collaborater> listCollaborater = new ArrayList<Collaborater>();
		for (CollaboraterEntity entity : collaboraterRepository.findByCollaboraterIdNotNull()) {
			listCollaborater.add(mapperCollab.mapToDomain(entity));
		}
		return listCollaborater;
	}

}
