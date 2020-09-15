package com.example.projetfilrouge.pskype.batch.referentiel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.ISiteEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.SiteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

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
	    	if (log.isErrorEnabled()){
	    		String sLogError = "Données du site incorrect en ligne : " + cptLigne;
				log.error(sLogError);
			}
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
