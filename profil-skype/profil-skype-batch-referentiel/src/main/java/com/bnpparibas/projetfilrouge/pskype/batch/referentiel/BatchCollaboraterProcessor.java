package com.bnpparibas.projetfilrouge.pskype.batch.referentiel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.CollaboraterDtoBatch;

import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ICollaboraterRepository;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.IOrganizationUnityRepository;

/**
 * Processor des collaborater
 * Mise à jour des données si le collaborateur est présent
 * @author Judicaël
 *
 */
public class BatchCollaboraterProcessor implements ItemProcessor<CollaboraterDtoBatch, CollaboraterEntity> {

	Logger log = LoggerFactory.getLogger(BatchCollaboraterProcessor.class);
	
	@Autowired
	private IOrganizationUnityRepository uoRepository;
	
	@Autowired
	private ICollaboraterRepository collaboraterRepository;
	
	@Override
	public CollaboraterEntity process(CollaboraterDtoBatch item) throws Exception {
		
		if(item.getCollaboraterId() == null) {
			log.error("Pas de collaborateur en entree");
		}
		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(item.getCollaboraterId());
		if (entity == null) {
			entity = new CollaboraterEntity();
			entity.setCollaboraterId(item.getCollaboraterId());
			if (item.getOrgaUnitCode()==null) {
				log.error("Pas de code uo "+item.getOrgaUnitCode()+" pour le collaborateur "+item.getCollaboraterId());
				return null;
			}
			entity.setOrgaUnit(uoRepository.findByOrgaUnityCode(item.getOrgaUnitCode()));
		}
		entity.setDeskPhoneNumber(item.getDeskPhoneNumber());
		entity.setFirstName(item.getFirstName());
		entity.setLastName(item.getLastName());
		entity.setMailAdress(item.getMailAdress());
		entity.setMobilePhoneNumber(item.getMobilePhoneNumber());
		return entity;
	}

}
