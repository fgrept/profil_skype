package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;

@Component
public class SiteEntityMapper extends AbstractMapper<Site, SiteEntity>{
	
	@Autowired
	private ISiteEntity siteEntity;
	
	@Override
	public Site mapToDomain(SiteEntity entity) {
		Site site = new Site(entity.getSiteCode(),entity.getSiteName(),entity.getSiteAddress(),
				entity.getSitePostalCode(),entity.getSiteCity());
		
		return site;
	}

	@Override
	public SiteEntity mapToEntity(Site dto) {
		
		SiteEntity entityRepo = getSiteEntityByCode(dto.getSiteCode());
		
		// on vérifie si l'entité existe en base et on la renvoie tel quel si c'est le cas
		// (problème de duplication sinon avec le cascade)
		if (entityRepo == null) {
			SiteEntity siteEntity = new SiteEntity();
			siteEntity.setSiteAddress(dto.getSiteAddress());
			siteEntity.setSiteCity(dto.getSiteCity());
			siteEntity.setSiteCode(dto.getSiteCode());
			siteEntity.setSiteName(dto.getSiteName());
			siteEntity.setSitePostalCode(dto.getSitePostalCode());			
			return siteEntity;
		} else return entityRepo;
	
	}
	
	private SiteEntity getSiteEntityByCode (String siteCode) {
		return siteEntity.findBySiteCode(siteCode);
	}
}
