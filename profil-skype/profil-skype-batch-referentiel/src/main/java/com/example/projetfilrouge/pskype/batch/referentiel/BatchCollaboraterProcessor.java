package com.example.projetfilrouge.pskype.batch.referentiel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.ICollaboraterRepository;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.IOrganizationUnityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.projetfilrouge.pskype.batch.referentiel.dto.CollaboraterDtoBatch;


/**
 * Processor des collaborater
 * Mise à jour des données si le collaborateur est présent
 * @author Judicaël
 *
 */
public class BatchCollaboraterProcessor implements ItemProcessor<CollaboraterDtoBatch, CollaboraterEntity> {

	Logger log = LoggerFactory.getLogger(BatchCollaboraterProcessor.class);
	
	private int cptLigne = 1;
	
	@Autowired
	private IOrganizationUnityRepository uoRepository;
	
	@Autowired
	private ICollaboraterRepository collaboraterRepository;
	
	@Override
	public CollaboraterEntity process(CollaboraterDtoBatch item) throws Exception {
		
		cptLigne +=1;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
		
	    Set<ConstraintViolation<CollaboraterDtoBatch>> constraintViolations = 
	    	      validator.validate(item);

	    if (constraintViolations.size() >0 ) {
	    	String sError = "Données du collaborateur incorrects en ligne : " + cptLigne;
	    	log.error(sError);
	    	log.error(constraintViolations.toString());
	    	return null;
	      }
		
		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(item.getCollaboraterId());
		if (entity == null) {
			entity = new CollaboraterEntity();
			entity.setCollaboraterId(item.getCollaboraterId());
			if (item.getOrgaUnitCode()==null) {
				String sError = "Pas de code uo "+item.getOrgaUnitCode()+" pour le collaborateur "+item.getCollaboraterId();
				log.error(sError);
				return null;
			}
			entity.setOrgaUnit(uoRepository.findByOrgaUnityCode(item.getOrgaUnitCode()));
		}
		entity.setDeskPhoneNumber(item.getDeskPhoneNumber());
		entity.setFirstName(item.getFirstName());
		entity.setLastName(item.getLastName());
		entity.setMailAdress(item.getMailAdress());
		entity.setMobilePhoneNumber(item.getMobilePhoneNumber());
		return entity;
	}

}
