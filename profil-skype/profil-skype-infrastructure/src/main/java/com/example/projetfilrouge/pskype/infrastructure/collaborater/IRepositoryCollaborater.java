package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author 116453
 * Un collaborateur peut être cherché par son id annuaire, ou son nom/prénom lors de la création d'un
 * profil skype ou lors de la création d'un utilisateur.
 *
 */
@Repository
public interface IRepositoryCollaborater extends JpaRepository<CollaboraterEntity, Long>{
	
	public CollaboraterEntity findByCollaboraterId(Long id);
	public List<CollaboraterEntity> findByFirstNameAndLastName(String firstname, String lastname);

}
