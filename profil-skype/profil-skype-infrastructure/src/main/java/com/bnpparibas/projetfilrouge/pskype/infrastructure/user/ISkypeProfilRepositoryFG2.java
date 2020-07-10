package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 *
 * @author 116453
 * 
 * Cette classe est un interface qui sera implémenté en Spring JDBC
 * pour permettre la construction dynamique de requête
 * lorsque l'utilisateur applique des filtres successifs
 * afin de chercher les profils qui l'intéressent.
 *
 */

public interface ISkypeProfilRepositoryFG2 {
	
	public List<SkypeProfileEntity> searchProfileByCriteria (List<String> criteria);
	
}
