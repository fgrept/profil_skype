package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;



	
/**
 * 
 * Mapper entre entité et domaine pour la classe Collaborater
 * @author Judicaël
 *
 */
@Component
public class CollaboraterEntityMapper extends AbstractMapper<Collaborater, CollaboraterEntity> {

	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe Collaborater de la classe Domaine
	 * @param L'entité Collaborateur de la couche de persistance
	 * @return l'objet Collaborateur de la classe de la couche Domain
	 */
	@Override
	public Collaborater mapToDomain(CollaboraterEntity entity) {
		
		Collaborater collaborater = new Collaborater(entity.getLastName(), entity.getFirstName(), entity.getCollaboraterId(), entity.getDeskPhoneNumber(), entity.getMobilePhoneNumber(), entity.getMailAdress());
		
		return collaborater;
	}

	/**
	 * Cette méthode récupère le contenu de la classe Collaborater de la classe Domain vers l'entité persistance de la couche infra  
	 * @param l'objet Collaborateur de la classe de la couche Domain
	 * @return L'entité Collaborateur de la couche de persistance
	 */
	@Override
	public CollaboraterEntity mapToEntity(Collaborater dto) {
		
		CollaboraterEntity entity = new CollaboraterEntity();
		entity.setCollaboraterId(dto.getCollaboraterId());
		entity.setFirstName(dto.getFirstNamePerson());
		entity.setLastName(dto.getLastNamePerson());
		entity.setDeskPhoneNumber(dto.getDeskPhoneNumber());
		entity.setMobilePhoneNumber(dto.getMobilePhoneNumber());
		entity.setMailAdress(dto.getMailAdress());
		
		return entity;
	}
}
