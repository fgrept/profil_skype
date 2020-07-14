package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntityMapper;

/**
 * 
 * Mapper entre entité et domaine pour la classe SkypeProfile
 * @author Judicaël
 *
 */
@Component
public class SkypeProfileEntityMapper extends AbstractMapper<SkypeProfile, SkypeProfileEntity> {

	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe ItCorrespondant de la classe Domaine
	 * @param L'entité SkypeProfileEntity de la couche de persistance
	 * @return l'objet SkypeProfile de la classe de la couche Domain
	 */
	@Autowired
	CollaboraterEntityMapper colloraterEntityMapper;
	
	@Override
	public SkypeProfile mapToDomain(SkypeProfileEntity entity) {
		
		System.out.println("SkypeProfileEntityMapper : mapToDomain");
		
		SkypeProfile skypeProfile = new SkypeProfile();
		skypeProfile.setSIP(entity.getSIP());
		skypeProfile.setDialPlan(entity.getDialPlan());
		skypeProfile.setEnterpriseVoiceEnabled(entity.isEnterpriseVoiceEnabled());
		skypeProfile.setExchUser(entity.getExchUser());
		skypeProfile.setExpirationDate(entity.getExpirationDate());
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
		
		System.out.println("SkypeProfileEntityMapper : mapToEntity");
		SkypeProfileEntity skypeProfileEntity = new SkypeProfileEntity();
	//	skypeProfileEntity.setCollaborater();
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
