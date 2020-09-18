package com.example.projetfilrouge.pskype.application;

import java.util.List;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.ICollaboraterDomain;
import com.example.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
	
	private static Logger logger = LoggerFactory.getLogger(CollaboraterManagementImpl.class);


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
			String msg = "findCollaboraterbyIdAnnuaire : id annuaire non renseigné en entrée";
			if (logger.isErrorEnabled()) {
				logger.error(msg);
			}
			throw new NotFoundException(ExceptionListEnum.NOTFOUND13, msg);
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

	@Override
	public Long countCollaborater() {
		return collaboraterDomain.countCollarater();
	}


}
