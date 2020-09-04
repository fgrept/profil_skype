package com.example.projetfilrouge.pskype.batch.referentiel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.projetfilrouge.pskype.infrastructure.user.IOrganizationUnityRepository;
import com.example.projetfilrouge.pskype.infrastructure.user.ISiteEntity;
import com.example.projetfilrouge.pskype.infrastructure.user.OrganizationUnityEntity;
import com.example.projetfilrouge.pskype.infrastructure.user.SiteEntity;
import com.example.projetfilrouge.pskype.batch.referentiel.dto.OrganizationUnityDtoBatch;


public class BatchUoProcessor implements ItemProcessor<OrganizationUnityDtoBatch, OrganizationUnityEntity>{
	
	Logger log = LoggerFactory.getLogger(BatchUoProcessor.class);
	private int cptLigne = 1;
	
	@Autowired
	private IOrganizationUnityRepository uoRepository;
	
	@Autowired
	private ISiteEntity siteRepository;
	
	@Override
	public OrganizationUnityEntity process(OrganizationUnityDtoBatch item) throws Exception {
		
		cptLigne +=1;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
		
	    Set<ConstraintViolation<OrganizationUnityDtoBatch>> constraintViolations = 
	    	      validator.validate(item);
	    if (constraintViolations.size() > 0 ) {
	    	log.error("Données de l'uo incorrects en ligne : " + cptLigne);
	    	return null;
	      }
	    
		OrganizationUnityEntity entity = uoRepository.findByOrgaUnityCode(item.getOrgaUnityCode());
		if (entity == null) {
			entity = new OrganizationUnityEntity();
			entity.setOrgaUnityCode(item.getOrgaUnityCode());
			SiteEntity siteEntity = siteRepository.findBySiteCode(item.getSiteCode());
			if (siteEntity ==null) {
				log.error("Pas de code site "+item.getSiteCode()+" pour uo "+item.getOrgaUnityCode());
				return null;
			}
			entity.setOrgaSite(siteEntity);
		}
		entity.setOrgaShortLabel(item.getOrgaShortLabel());
		entity.setOrgaUnityType(item.getOrgaUnityType());
		
		return entity;
	}

}
