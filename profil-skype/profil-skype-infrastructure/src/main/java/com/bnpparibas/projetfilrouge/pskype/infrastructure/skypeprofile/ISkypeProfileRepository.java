package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;

@Repository
public interface ISkypeProfileRepository extends JpaRepository<SkypeProfileEntity, Long>, JpaSpecificationExecutor<SkypeProfileEntity> {

	SkypeProfileEntity findBySIP(String sip);

	List<SkypeProfileEntity> findBySIPNotNull();
	
	SkypeProfileEntity findBySIPAndStatusProfile(String SIP, StatusSkypeProfileEnum status);
	
	SkypeProfileEntity findByCollaborater(CollaboraterEntity collaboraterEntity);
	
}
