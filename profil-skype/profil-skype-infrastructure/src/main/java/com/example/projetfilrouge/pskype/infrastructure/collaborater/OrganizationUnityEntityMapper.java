package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.projetfilrouge.pskype.infrastructure.AbstractMapper;

@Component
public class OrganizationUnityEntityMapper extends AbstractMapper<OrganizationUnity, OrganizationUnityEntity>{
	
	@Autowired
	SiteEntityMapper siteMapper;
	@Autowired
    IOrganizationUnityRepository orgaUnityRepo;
	
	@Override
	public OrganizationUnity mapToDomain(OrganizationUnityEntity entity) {
		
		if (entity !=null) {
			return new OrganizationUnity(entity.getOrgaUnityCode(),
			entity.getOrgaUnityType(), entity.getOrgaShortLabel(), siteMapper.mapToDomain(entity.getOrgaSite()));
		}else {
			return null;
		}
	}

	@Override
	public OrganizationUnityEntity mapToEntity(OrganizationUnity dto) {
		
		OrganizationUnityEntity uoEntityRepo = getOrganizationUnityByCode(dto.getOrgaUnityCode());
		
		// on vérifie si l'entité existe en base et on la renvoie tel quel si c'est le cas
		// (problème de duplication sinon avec le cascade)
		if (uoEntityRepo == null) {
			if (dto.getOrgaSite()!=null) {
				return new OrganizationUnityEntity(dto.getOrgaUnityCode(),dto.getOrgaUnityType(),dto.getOrgaShortLabel(),
						siteMapper.mapToEntity(dto.getOrgaSite()));
			}
			return new OrganizationUnityEntity(dto.getOrgaUnityCode(),dto.getOrgaUnityType(),dto.getOrgaShortLabel(),
					null);
		} else return uoEntityRepo;

	}
	
	private OrganizationUnityEntity getOrganizationUnityByCode (String orgaUnityCode) {
		return orgaUnityRepo.findByOrgaUnityCode(orgaUnityCode);

	}
}
