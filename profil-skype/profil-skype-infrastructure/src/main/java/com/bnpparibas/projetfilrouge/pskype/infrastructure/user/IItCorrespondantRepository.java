package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




/**
 * 
 * Liste des méthodes du CIL exposées à la couche application
 * @author Judicael
 *
 */
@Repository
public interface IItCorrespondantRepository extends JpaRepository <ItCorrespondantEntity, Long>{
//	void create(ItCorrespondantEntity itc);
	List<ItCorrespondantEntity> findAllItCorrespondantEntities();
	ItCorrespondantEntity findByCollaboraterId(String id);
//	List<ItCorrespondantEntity> recupItCorrespondantByName(String lastName,String firstName);
//	ItCorrespondantEntity recupItCorrespondantByIdAgent(String id);
}
