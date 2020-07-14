package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ICollaboraterRepository;

/**
 * Dédiée au profil Skype
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - Création d'un profil skype (US012)
 * - Mise à jour d'un profil skype (US005)
 * - Suppression d'un CIL en base (US006)
 * - Afficher la liste des profils skype - mode Full (US001)
 * - Afficher la liste des profils skype - selon critères (US001)
 * @author Judicaël
 *
 */

@Repository
public class SkypeProfileRepositoryImpl implements ISkypeProfileDomain{

	
	@Autowired
	private SkypeProfileEntityMapper entityMapper;

	@Autowired
	private ISkypeProfileRepository skypeProfileRepository;
	
	@Autowired
	private ICollaboraterRepository collaboraterRepository;	
	
	/**
	 * La création est possible uniquement si :
	 * - Le SIP (adresse mail skype) n'existe pas déjà.
	 * - Le collaborateur associé au profil skype n'a pas encore de profil.
	 * @param SkypeProfile skypeProfile
	 */
	@Override
	public void create(SkypeProfile skypeProfile) {
		
		System.out.println("SkypeProfileRepositoryImpl : create");
//		System.out.println("SkypeProfileRepositoryImpl : "+ skypeProfile.getCollaborater().getCollaboraterId());
		SkypeProfileEntity entity = skypeProfileRepository.findBySIP(skypeProfile.getSIP());
		if (entity==null) {
			entity = entityMapper.mapToEntity(skypeProfile);
//			System.out.println("SkypeProfileRepositoryImpl : après mapping");
			entity.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
//			System.out.println("SkypeProfileRepositoryImpl : avant récupération collaborateur");
			CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId());
			if (skypeProfileRepository.findByCollaborater(collaboraterEntity)==null) {
				entity.setCollaborater(collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));
				System.out.println("SkypeProfileRepositoryImpl : avant sauvegarde");
				skypeProfileRepository.save(entity);
			}else {
				throw new RuntimeException(skypeProfile.getCollaborater().getCollaboraterId()+" a déjà un profil skype");
			}

		}else {
			throw new RuntimeException("Profil Skype "+skypeProfile.getSIP()+" existe déjà");
		}
	}


	@Override
	public SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status) {
		// TODO Auto-generated method stub
		return entityMapper.mapToDomain(skypeProfileRepository.findBySIPAndStatusProfile(sip, status));
	}

	@Override
	public void update(SkypeProfile SkypeProfile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SkypeProfile SkypeProfile) {
		
		SkypeProfileEntity entity = entityMapper.mapToEntity(SkypeProfile);
		skypeProfileRepository.delete(entity);
		
	}


	@Override
	public List<SkypeProfile> findAllSkypeProfile() {
		// TODO Auto-generated method stub
		List<SkypeProfile> listSkypeProfile = new ArrayList<SkypeProfile>();
		for (SkypeProfileEntity entity:skypeProfileRepository.findBySIPNotNull()) {
			listSkypeProfile.add(entityMapper.mapToDomain(entity));
		}
		return listSkypeProfile;
	}


	@Override
	public List<SkypeProfile> findSkypeProfileFilters() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SkypeProfile findSkypeProfileBySip(String sip) {
		// TODO Auto-generated method stub
		return entityMapper.mapToDomain(skypeProfileRepository.findBySIP(sip));
	}

}
