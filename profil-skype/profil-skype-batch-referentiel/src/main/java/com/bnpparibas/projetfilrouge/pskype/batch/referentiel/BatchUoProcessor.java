package com.bnpparibas.projetfilrouge.pskype.batch.referentiel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.OrganizationUnityDtoBatch;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.IOrganizationUnityRepository;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ISiteEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.OrganizationUnityEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.SiteEntity;


public class BatchUoProcessor implements ItemProcessor<OrganizationUnityDtoBatch, OrganizationUnityEntity>{

	
	Logger log = LoggerFactory.getLogger(BatchUoProcessor.class);
	
	@Autowired
	private IOrganizationUnityRepository uoRepository;
	
	@Autowired
	private ISiteEntity siteRepository;
	
	@Override
	public OrganizationUnityEntity process(OrganizationUnityDtoBatch item) throws Exception {
		
		if (item.getOrgaUnityCode()==null) {
			log.error("Pas de code uo en entree");
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
