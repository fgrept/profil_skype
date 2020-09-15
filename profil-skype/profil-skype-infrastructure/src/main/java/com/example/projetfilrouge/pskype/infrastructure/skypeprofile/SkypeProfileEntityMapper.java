package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfile;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.example.projetfilrouge.pskype.infrastructure.AbstractMapper;


/**
 * 
 * Mapper entre entité et domaine pour la classe SkypeProfile
 * @author Judicaël
 *
 */
@Component
public class SkypeProfileEntityMapper extends AbstractMapper<SkypeProfile, SkypeProfileEntity> {
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileEntityMapper.class);
	
	@Autowired
	CollaboraterEntityMapper colloraterEntityMapper;
	
	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe ItCorrespondant de la classe Domaine
	 * @param entity L'entité SkypeProfileEntity de la couche de persistance
	 * @return l'objet SkypeProfile de la classe de la couche Domain
	 */	
	@Override
	public SkypeProfile mapToDomain(SkypeProfileEntity entity) {
		
		logger.debug("SkypeProfileEntityMapper : mapToDomain");
		Collaborater collaborater=null;
		if (entity.getCollaborater()!=null) {
			collaborater = colloraterEntityMapper.mapToDomain(entity.getCollaborater());
		}

		return new SkypeProfile(entity.getSIP(),
				entity.isEnterpriseVoiceEnabled(),
				entity.getVoicePolicy(),
				entity.getDialPlan(),
				entity.getSamAccountName(),
				entity.isExUmEnabled(),
				entity.getExchUser(),
				entity.getObjectClass(),
				collaborater,
				entity.getStatusProfile(),
				entity.getExpirationDate());
	}
	
	/**
	 * Cette méthode récupère le contenu de la classe SkypeProfile de la classe Domain vers l'entité persistance de la couche infra  
	 * @param dto l'objet SkypeProfile de la classe de la couche Domain
	 * @return SkypeProfileEntity L'entité SkypeProfileEntity de la couche de persistance
	 */
	@Override
	public SkypeProfileEntity mapToEntity(SkypeProfile dto) {
		
		logger.debug("SkypeProfileEntityMapper : mapToEntity");
		CollaboraterEntity collaboraterEntity=null;
		if (dto.getCollaborater()!=null){
			collaboraterEntity = colloraterEntityMapper.mapToEntity(dto.getCollaborater());
		}

		return new SkypeProfileEntity(dto.getSIP(),
				dto.isEnterpriseVoiceEnabled(),
				dto.getVoicePolicy(),
				dto.getDialPlan(),
				dto.getSamAccountName(),
				dto.isExUmEnabled(),
				dto.getExchUser(),
				dto.getObjectClass(),
				dto.getStatusProfile(),
				dto.getExpirationDate(),
				collaboraterEntity);

	}

}
