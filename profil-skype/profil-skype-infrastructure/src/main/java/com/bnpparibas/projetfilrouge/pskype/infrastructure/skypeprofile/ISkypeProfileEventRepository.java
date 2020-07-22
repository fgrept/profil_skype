package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISkypeProfileEventRepository extends JpaRepository<SkypeProfileEventEntity, Long>, JpaSpecificationExecutor<SkypeProfileEventEntity> {
	List<SkypeProfileEventEntity> findBySkypeProfile(SkypeProfileEntity skypeProfile);
	List<SkypeProfileEventEntity> findBySIP(String SIP);
}
