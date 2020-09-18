package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISiteEntity extends JpaRepository<SiteEntity, Long>{
	
	public SiteEntity findBySiteCode (String siteCode);
}
