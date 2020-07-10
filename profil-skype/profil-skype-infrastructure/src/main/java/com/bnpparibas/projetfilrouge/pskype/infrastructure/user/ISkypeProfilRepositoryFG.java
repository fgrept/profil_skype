package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 *
 * @author 116453
 * 
 * Cette classe fonctionnera mais on ne pourra pas additionner les paramètres de filtre
 * suite aux choix des utilisateurs
 * Elle est implémentée par Spring Boot Data en JPA au moment de l'injection
 * de dépendances
 *
 */

@Repository
public interface ISkypeProfilRepositoryFG extends JpaRepository<SkypeProfileEntity, Long>{
	
	@Query("select p from SkypeProfileEntity p where "
			+ "p.collaborater.orgaUnit.orgaShortLabel = :name")
	List<SkypeProfileEntity> searchProfileByUoName (@Param("name") String nameUo);

	@Query("select p from SkypeProfileEntity p where "
			+ "p.collaborater.orgaUnit.orgaUnityCode = :name")
	List<SkypeProfileEntity> searchProfileByUoCode (@Param("name") String codeUo);
	
	@Query("select p from SkypeProfileEntity p where "
			+ "p.collaborater.orgaUnit.orgaSite.siteName = :name")
	List<SkypeProfileEntity> searchProfileBySiteName (@Param("name") String nameSite);
	
	
}
