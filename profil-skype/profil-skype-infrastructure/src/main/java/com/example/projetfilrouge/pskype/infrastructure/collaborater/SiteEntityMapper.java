package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.projetfilrouge.pskype.infrastructure.AbstractMapper;

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
			return new SiteEntity(dto.getSiteCode(),dto.getSiteName(),dto.getSiteAddress(),dto.getSitePostalCode(),dto.getSiteCity());

		} else return entityRepo;
	
	}
	
	private SiteEntity getSiteEntityByCode (String siteCode) {
		return siteEntity.findBySiteCode(siteCode);
	}
}
