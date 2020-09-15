package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationUnityRepository extends JpaRepository<OrganizationUnityEntity, Long>{
	
	public OrganizationUnityEntity findByOrgaUnityCode (String orgaUnityCode);
}
