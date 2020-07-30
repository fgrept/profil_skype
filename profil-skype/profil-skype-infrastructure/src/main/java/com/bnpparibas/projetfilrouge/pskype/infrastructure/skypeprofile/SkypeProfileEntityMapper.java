package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntityMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantRepositoryImpl;

/**
 * 
 * Mapper entre entité et domaine pour la classe SkypeProfile
 * @author Judicaël
 *
 */
@Component
public class SkypeProfileEntityMapper extends AbstractMapper<SkypeProfile, SkypeProfileEntity> {
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantRepositoryImpl.class);
	
	@Autowired
	CollaboraterEntityMapper colloraterEntityMapper;
	
	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe ItCorrespondant de la classe Domaine
	 * @param L'entité SkypeProfileEntity de la couche de persistance
	 * @return l'objet SkypeProfile de la classe de la couche Domain
	 */	
	@Override
	public SkypeProfile mapToDomain(SkypeProfileEntity entity) {
		
		logger.debug("SkypeProfileEntityMapper : mapToDomain");
		
		SkypeProfile skypeProfile = new SkypeProfile();
		skypeProfile.setSIP(entity.getSIP());
		skypeProfile.setDialPlan(entity.getDialPlan());
		skypeProfile.setEnterpriseVoiceEnabled(entity.isEnterpriseVoiceEnabled());
		skypeProfile.setExchUser(entity.getExchUser());
	//	skypeProfile.setExpirationDate(entity.getExpirationDate());
		skypeProfile.setExUmEnabled(entity.isExUmEnabled());
		skypeProfile.setObjectClass(entity.getObjectClass());
		skypeProfile.setSamAccountName(entity.getSamAccountName());
		skypeProfile.setStatusProfile(entity.getStatusProfile());
		skypeProfile.setVoicePolicy(entity.getVoicePolicy());
		skypeProfile.setCollaborater(colloraterEntityMapper.mapToDomain(entity.getCollaborater()));
		return skypeProfile;
	}
	
	/**
	 * Cette méthode récupère le contenu de la classe SkypeProfile de la classe Domain vers l'entité persistance de la couche infra  
	 * @param l'objet SkypeProfile de la classe de la couche Domain
	 * @return L'entité SkypeProfileEntity de la couche de persistance
	 */
	@Override
	public SkypeProfileEntity mapToEntity(SkypeProfile dto) {
		
		logger.debug("SkypeProfileEntityMapper : mapToEntity");
		SkypeProfileEntity skypeProfileEntity = new SkypeProfileEntity();
		skypeProfileEntity.setCollaborater(colloraterEntityMapper.mapToEntity(dto.getCollaborater()));
		skypeProfileEntity.setDialPlan(dto.getDialPlan());
		skypeProfileEntity.setEnterpriseVoiceEnabled(dto.isEnterpriseVoiceEnabled());
		skypeProfileEntity.setExchUser(dto.getExchUser());
		skypeProfileEntity.setExpirationDate(dto.getExpirationDate());
		skypeProfileEntity.setExUmEnabled(dto.isExUmEnabled());
		skypeProfileEntity.setObjectClass(dto.getObjectClass());
		skypeProfileEntity.setSamAccountName(dto.getSamAccountName());
		skypeProfileEntity.setSIP(dto.getSIP());
		skypeProfileEntity.setStatusProfile(dto.getStatusProfile());
		skypeProfileEntity.setVoicePolicy(dto.getVoicePolicy());
		return skypeProfileEntity;
	}

}
