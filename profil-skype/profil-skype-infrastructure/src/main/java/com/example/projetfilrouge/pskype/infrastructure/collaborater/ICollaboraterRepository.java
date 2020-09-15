package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * Liste des méthodes de type dao du collaborateur 
 * @author Judicael
 *
 */

@Repository
public interface ICollaboraterRepository extends JpaRepository<CollaboraterEntity, Long>{

	List<CollaboraterEntity> findByLastNameAndFirstName(String lastName,String firstName);
	CollaboraterEntity findByCollaboraterId(String id);
	List<CollaboraterEntity> findByCollaboraterIdNotNull();
	List<CollaboraterEntity> findByCollaboraterIdNotNull(Pageable pageable);
	List<CollaboraterEntity> findAll(Specification<CollaboraterEntity> specification, Pageable pageable);
}