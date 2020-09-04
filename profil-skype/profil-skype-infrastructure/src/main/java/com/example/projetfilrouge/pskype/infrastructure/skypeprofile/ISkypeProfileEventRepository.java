package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Interface définissant les méthodes spécifiques à implémenter via JPA
 *  
 * @author La Fabrique
 *
 */
public interface ISkypeProfileEventRepository extends JpaRepository<SkypeProfileEventEntity, Long>, JpaSpecificationExecutor<SkypeProfileEventEntity> {
	/**
	 * Lister tous les événements d'un profil skype
	 * @param skypeProfile
	 * @return une liste d'événements
	 */
	List<SkypeProfileEventEntity> findBySkypeProfileSIP(String SIP);
	
	/**
	 * récupérer les événements relatifs à un It Correspondant
	 * @param collaboraterId
	 * @return une liste d'événements
	 */
	List<SkypeProfileEventEntity> findByItCorrespondantItCorrespondantId(String itCorrespondant);
}
