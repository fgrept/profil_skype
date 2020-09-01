package com.bnpparibas.projetfilrouge.pskype.batch.referentiel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ISiteEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.SiteEntity;
/**
 * Processor des sites
 * Mise à jour des données si le site est présent
 * @author Judicaël
 */
public class BatchSiteProcessor implements ItemProcessor<Site, SiteEntity>{

	Logger log = LoggerFactory.getLogger(BatchSiteProcessor.class);
	
	private int cptLigne = 1;
	
	@Autowired
	private ISiteEntity siteRepository;
	
	@Override
	public SiteEntity process(Site site) throws Exception {
		
		cptLigne +=1;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
		
	    Set<ConstraintViolation<Site>> constraintViolations = 
	    	      validator.validate(site);
	    if (constraintViolations.size() > 0 ) {
	    	log.error("Données du site incorrect en ligne : " + cptLigne);
	    	return null;
	      }
		
		SiteEntity entity = siteRepository.findBySiteCode(site.getSiteCode());
		if (entity == null) {
			entity = new SiteEntity();
			entity.setSiteCode(site.getSiteCode());
		}
		entity.setSiteAddress(site.getSiteAddress());
		entity.setSiteCity(site.getSiteCity());
		entity.setSiteName(site.getSiteName());
		entity.setSitePostalCode(site.getSitePostalCode());
		return entity;
	}


}
