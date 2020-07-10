package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.CollaboraterWithAffectation;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboratorManagement;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;

@Repository
public class RepositoryCollaboraterImpl implements ICollaboratorManagement {
	
	@Autowired
	IRepositoryCollaborater dao;
	
	@Autowired
	CollaboraterEntityMapper mapperCollab;
	
	@Autowired
	SiteEntityMapper mapperSite;
	
	@Autowired
	OrganizationUnityEntityMapper mapperOrga;

	@Override
	public CollaboraterWithAffectation searchById(Long id) {
		
		CollaboraterEntity collabEntity = dao.findByCollaboraterId(id);
		
		CollaboraterWithAffectation collabWithAffect = new CollaboraterWithAffectation();
		collabWithAffect.setCollab(mapperCollab.mapToDomain(collabEntity));
		
		OrganizationUnityEntity orgaUnitEntity = collabEntity.getOrgaUnit();
		collabWithAffect.setCollabUO(mapperOrga.mapToDomain(orgaUnitEntity));
		
		SiteEntity siteEntity = collabEntity.getOrgaUnit().getOrgaSite();
		collabWithAffect.setCollabSite(mapperSite.mapToDomain(siteEntity));
		
		return collabWithAffect;
	}

	@Override
	public List<CollaboraterWithAffectation> searchByName(String firstname, String lastname) {
			
		List<CollaboraterEntity> entityCollabList = dao.findByFirstNameAndLastName(firstname, lastname);
		
		List<CollaboraterWithAffectation> collabWithAffectList = new ArrayList<>();
		
		for (CollaboraterEntity collabEntity : entityCollabList) {
			CollaboraterWithAffectation collabWithAffect = new CollaboraterWithAffectation();
			collabWithAffect.setCollab((mapperCollab.mapToDomain(collabEntity)));
			
			OrganizationUnityEntity orgaUnitEntity = collabEntity.getOrgaUnit();
			collabWithAffect.setCollabUO(mapperOrga.mapToDomain(orgaUnitEntity));
			
			SiteEntity siteEntity = collabEntity.getOrgaUnit().getOrgaSite();
			collabWithAffect.setCollabSite(mapperSite.mapToDomain(siteEntity));
			
			collabWithAffectList.add(collabWithAffect);
		}
		return collabWithAffectList;
	}

	
	
}
