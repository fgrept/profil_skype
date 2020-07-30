package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * 
 * Liste des méthodes de type dao du CIL 
 * @author Judicael
 *
 */

@Repository
public interface IItCorrespondantRepository extends JpaRepository <ItCorrespondantEntity, Long>, JpaSpecificationExecutor<ItCorrespondantEntity>{

	List<ItCorrespondantEntity> findByItCorrespondantIdNotNull();
	ItCorrespondantEntity findByCollaboraterCollaboraterId(String id);

}
