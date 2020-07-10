package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * Liste des méthodes du collaborateur exposées à la couche application
 * @author Judicael
 *
 */

@Repository
public interface ICollaboraterRepository extends JpaRepository<CollaboraterEntity, Long>{
//	S save (Collaborater collaborater);
	List<CollaboraterEntity> findByLastNameAndFirstName(String lastName,String firstName);
	CollaboraterEntity findByCollaboraterId(String id);
}