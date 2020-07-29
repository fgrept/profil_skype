package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
/**
 * Service exposant les méthodes d'interfaction avec le Collaborateur
 * @author Judicaël
 *
 */
@Service
@Transactional
public class CollaboraterManagementImpl implements ICollaboraterManagment {

	@Autowired
	private ICollaboraterDomain collaboraterDomain;
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantManagementImpl.class);


	@Override
	public boolean createCollaborater(Collaborater collaborater) {
		return collaboraterDomain.create(collaborater);
	}
	@Override
	public List<Collaborater> listCollaborater() {
		
		return collaboraterDomain.findAllCollaborater();
	}

	@Override
	public Collaborater findCollaboraterbyIdAnnuaire(String idAnnuaire) {
		
		if (idAnnuaire != null) {
			return collaboraterDomain.findByCollaboraterId(idAnnuaire);
		}else {
			logger.error("findCollaboraterbyIdAnnuaire : id annuaire non renseigné en entrée");
			return null;
		}
		
	}
	@Override
	public List<Collaborater> listCollaboraterSortByPage(int numberPage, int sizePage, String attribute,
			boolean sortAscending) {
		
		return collaboraterDomain.findAllCollaboraterPage(numberPage, sizePage, attribute, sortAscending);
	}
	
	
	@Override
	public List<Collaborater> listCollaboraterCriteriaSortByPage(Collaborater collaborater, int numberPage,
			int sizePage, String attribute, boolean sortAscending) {
		
		return collaboraterDomain.findAllCollaboraterCriteriaPage(collaborater, numberPage, sizePage, attribute, sortAscending);
	}



}
