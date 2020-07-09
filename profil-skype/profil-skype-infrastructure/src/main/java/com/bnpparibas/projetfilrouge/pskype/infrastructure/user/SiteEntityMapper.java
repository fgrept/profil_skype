package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;

public class SiteEntityMapper extends AbstractMapper<Site, SiteEntity>{

	@Override
	public Site mapToDomain(SiteEntity entity) {
		Site site = new Site(entity.getSiteCode(),entity.getSiteName(),entity.getSiteAddress(),
				entity.getSitePostalCode(),entity.getSiteCity());
		
		return site;
	}

	@Override
	public SiteEntity mapToEntity(Site dto) {

		return null;
	}

}
