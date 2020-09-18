package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.List;

import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * 
 * @author stagiaire
 *
 */

@Repository
public interface ISkypeProfileRepository extends JpaRepository<SkypeProfileEntity, Long>, JpaSpecificationExecutor<SkypeProfileEntity> {
	
	/**
	 * Méthode de recherche d'un profil skype à partir du SIP
	 * @param sip
	 * @return un profil skype
	 */
	SkypeProfileEntity findBySIP(String sip);
	
	/**
	 * Méthode permettant de lister tous les profils Skype
	 * @return une liste de profil skype
	 */
	List<SkypeProfileEntity> findBySIPNotNull();
	
	/**
	 * Méthode permettant de lister tous les profils Skype par pagination
	 * @return une liste de profil skype
	 */
	List<SkypeProfileEntity> findBySIPNotNull(Pageable pageable);
	
	
	/**
	 * Méthode de consultation d'un profil Skype à partir du SIP et du Status du profil
	 * @param sip
	 * @param status
	 * @return un profil skype
	 */
	SkypeProfileEntity findBySIPAndStatusProfile(String sip, StatusSkypeProfileEnum status);
	
	
	/**
	 * Méthode de recherche d'un profil skype à partir d'un objet Collaborater
	 * @param collaboraterEntity un collaborater
	 * @return un objet profil skype
	 */
	SkypeProfileEntity findByCollaborater(CollaboraterEntity collaboraterEntity);
	
	/**
	 * Méthode de recherche d'un profil skype à partir de l'Id annuaire
	 * @param idAnnuaire
	 * @return un objet profil skype
	 */
	@Query(value = "select p from SkypeProfileEntity p where p.collaborater.collaboraterId = :id", nativeQuery = false)
	SkypeProfileEntity getSkypeProfilByIdCollab (@Param("id") String idAnnuaire);
	
		
}
