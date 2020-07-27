


package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;

/**
 * 
 * Mapper entre entité et domaine pour la classe ItCorrespondant
 * @author Judicaël
 * Spring Security : ajout du password
 */

@Component
public class ItCorrespondantEntityMapper extends AbstractMapper<ItCorrespondant, ItCorrespondantEntity> {
	
	@Autowired
	OrganizationUnityEntityMapper orgaMapper;
	
	/**
	 * Cette méthode récupère le contenu de la couche infra vers la classe ItCorrespondant de la classe Domaine
	 * @param L'entité ItCorrespondantEntity de la couche de persistance
	 * @return l'objet ItCorrespondant de la classe de la couche Domain
	 */
	
	@Override
	public ItCorrespondant mapToDomain(ItCorrespondantEntity entity) {
		
		ItCorrespondant itCorrespondant = new ItCorrespondant(entity.getCollaborater().getLastName(), 
				entity.getCollaborater().getFirstName(), entity.getCollaborater().getCollaboraterId(), entity.getCollaborater().getDeskPhoneNumber(), 
				entity.getCollaborater().getMobilePhoneNumber(), entity.getCollaborater().getMailAdress(),orgaMapper.mapToDomain(entity.getCollaborater().getOrgaUnit()));
		itCorrespondant.setRoles(entity.getRoles());
		// pour spring security
		itCorrespondant.setPassword(entity.getEncryptedPassword());
		
		return itCorrespondant;
	}

	/**
	 * Cette méthode récupère le contenu de la classe ItCorrespondant de la classe Domain vers l'entité persistance de la couche infra  
	 * @param l'objet ItCorrespondant de la classe de la couche Domain
	 * @return L'entité ItCorrespondantEntity de la couche de persistance
	 */
	@Override
	public ItCorrespondantEntity mapToEntity(ItCorrespondant dto) {
		
		CollaboraterEntity entityColl = new CollaboraterEntity(dto.getLastNamePerson(), dto.getFirstNamePerson(), dto.getCollaboraterId(), dto.getDeskPhoneNumber(), dto.getMobilePhoneNumber(), dto.getMailAdress());
		ItCorrespondantEntity entity = new ItCorrespondantEntity(entityColl,dto.getCollaboraterId(),dto.getPassword());
		entity.setRoles(dto.getRoles());

		
		if (dto.getOrgaUnit()!=null) {
			entity.getCollaborater().setOrgaUnit(orgaMapper.mapToEntity(dto.getOrgaUnit()));
		}

		
		return entity;
	}

}
