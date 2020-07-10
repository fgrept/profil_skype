package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;

public class OrganizationUnityEntityMapper extends AbstractMapper<OrganizationUnity, OrganizationUnityEntity>{
	
	@Autowired
	SiteEntityMapper siteMapper;
	
	@Override
	public OrganizationUnity mapToDomain(OrganizationUnityEntity entity) {

		OrganizationUnity uo = new OrganizationUnity(entity.getOrgaUnityCode(),
				entity.getOrgaUnityType(), entity.getOrgaShortLabel(), siteMapper.mapToDomain(entity.getOrgaSite()));
		
		return uo;
	}

	@Override
	public OrganizationUnityEntity mapToEntity(OrganizationUnity dto) {

		return null;
	}

}
