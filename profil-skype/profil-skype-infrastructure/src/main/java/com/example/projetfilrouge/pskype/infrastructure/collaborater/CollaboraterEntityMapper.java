package com.example.projetfilrouge.pskype.infrastructure.collaborater;


import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.example.projetfilrouge.pskype.infrastructure.AbstractMapper;



	
/**
 * 
 * Mapper entre entité et domaine pour la classe Collaborater
 * @author Judicaël
 *
 */
@Component
public class CollaboraterEntityMapper extends AbstractMapper<Collaborater, CollaboraterEntity> {
	
	@Autowired
    OrganizationUnityEntityMapper orgaMapper;
	
	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe Collaborater de la classe Domaine
	 * @param entity L'entité Collaborateur de la couche de persistance
	 * @return Collaborater l'objet Collaborateur de la classe de la couche Domain
	 */
	@Override
	public Collaborater mapToDomain(CollaboraterEntity entity) {
		
		return new Collaborater(entity.getLastName(),
				entity.getFirstName(), entity.getCollaboraterId(), 
				entity.getDeskPhoneNumber(), entity.getMobilePhoneNumber(), 
				entity.getMailAdress(), orgaMapper.mapToDomain(entity.getOrgaUnit()));

	}

	/**
	 * Cette méthode récupère le contenu de la classe Collaborater de la classe Domain vers l'entité persistance de la couche infra  
	 * @param dto l'objet Collaborateur de la classe de la couche Domain
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
		if (dto.getOrgaUnit()!=null) {
			entity.setOrgaUnit(orgaMapper.mapToEntity(dto.getOrgaUnit()));
		}
		
		return entity;
	}

}
