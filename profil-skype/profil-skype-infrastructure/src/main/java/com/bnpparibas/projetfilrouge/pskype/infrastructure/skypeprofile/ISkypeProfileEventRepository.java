package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

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
	List<SkypeProfileEventEntity> findBySkypeProfileSIP(String SIP);
}
