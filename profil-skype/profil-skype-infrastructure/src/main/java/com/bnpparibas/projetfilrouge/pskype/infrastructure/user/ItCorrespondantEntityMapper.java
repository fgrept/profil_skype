


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
		
		ItCorrespondant itCorrespondant = new ItCorrespondant(entity.getLastName(), entity.getFirstName(), entity.getCollaboraterId(), entity.getDeskPhoneNumber(), entity.getMobilePhoneNumber(), entity.getMailAdress(),orgaMapper.mapToDomain(entity.getOrgaUnit()));
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
		
		ItCorrespondantEntity entity = new ItCorrespondantEntity();
		entity.setCollaboraterId(dto.getCollaboraterId());
		entity.setFirstName(dto.getFirstNamePerson());
		entity.setLastName(dto.getLastNamePerson());
		entity.setDeskPhoneNumber(dto.getDeskPhoneNumber());
		entity.setMobilePhoneNumber(dto.getMobilePhoneNumber());
		entity.setMailAdress(dto.getMailAdress());
		entity.setRoles(dto.getRoles());
		entity.setOrgaUnit(orgaMapper.mapToEntity(dto.getOrgaUnit()));
		// pour spring security
		entity.setEncryptedPassword(dto.getPassword());
		
		return entity;
	}

}
