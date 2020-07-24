package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntityMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ICollaboraterRepository;

/**
 * Dédiée au profil Skype Elle assure la correspondance entre les méthodes
 * exposées de la couche domaine et celles da la couche infrastructure liste des
 * méthodes : - Création d'un profil skype (US012) - Mise à jour d'un profil
 * skype (US005) - Suppression d'un profil Skype en base (US006) - Afficher la
 * liste des profils skype - mode Full (US001) - Afficher la liste des profils
 * skype - selon critères (US001)
 * 
 * @author Judicaël
 *
 */

@Repository
public class SkypeProfileRepositoryImpl implements ISkypeProfileDomain {

	@Autowired
	private SkypeProfileEntityMapper entityMapperSkypeProfile;

	@Autowired
	private CollaboraterEntityMapper entityMapperCollaborater;

	@Autowired
	private ISkypeProfileRepository skypeProfileRepository;

	@Autowired
	private ISkypeProfileEventRepository skypeProfileEventRepository;

	@Autowired
	private ICollaboraterRepository collaboraterRepository;

	/**
	 * La création est possible uniquement si : - Le SIP (adresse mail skype)
	 * n'existe pas déjà. - Le collaborateur associé au profil skype n'a pas encore
	 * de profil.
	 * 
	 * @param SkypeProfile skypeProfile
	 */
	@Override
	public void create(SkypeProfile skypeProfile) {

		System.out.println("SkypeProfileRepositoryImpl : create");
//		System.out.println("SkypeProfileRepositoryImpl : "+ skypeProfile.getCollaborater().getCollaboraterId());
		SkypeProfileEntity entity = skypeProfileRepository.findBySIP(skypeProfile.getSIP());
		if (entity == null) {
			entity = entityMapperSkypeProfile.mapToEntity(skypeProfile);
//			System.out.println("SkypeProfileRepositoryImpl : après mapping");
			entity.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
//			System.out.println("SkypeProfileRepositoryImpl : avant récupération collaborateur");
			CollaboraterEntity collaboraterEntity = collaboraterRepository
					.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId());
			if (skypeProfileRepository.findByCollaborater(collaboraterEntity) == null) {
				entity.setCollaborater(collaboraterRepository
						.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));
				System.out.println("SkypeProfileRepositoryImpl : avant sauvegarde");
				skypeProfileRepository.save(entity);
			} else {
				throw new RuntimeException(
						skypeProfile.getCollaborater().getCollaboraterId() + " a déjà un profil skype");
			}

		} else {
			throw new RuntimeException("Profil Skype " + skypeProfile.getSIP() + " existe déjà");
		}
	}

	@Override
	public SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status) {
		// TODO Auto-generated method stub
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findBySIPAndStatusProfile(sip, status));
	}

	// US006
	@Override
	public void delete(String sip) {

		// Récupérer le profil Skype à partir de l'identifiant SIP

		SkypeProfileEntity skypeProfile = skypeProfileRepository.findBySIP(sip);

		if (skypeProfile == null) {
			throw new RuntimeException("Profil skype non trouvé , SIP : " + sip);
		} else {

			// Avant la suppresion du profil Skype, on supprime d'abord les événements
			// correspondant.

			skypeProfileEventRepository.deleteAll(skypeProfileEventRepository.findBySkypeProfile(skypeProfile));

			// Suppression du profil Skype
			skypeProfileRepository.delete(skypeProfile);

		}

	}

	@Override
	public List<SkypeProfile> findAllSkypeProfile() {
		// TODO Auto-generated method stub
		List<SkypeProfile> listSkypeProfile = new ArrayList<SkypeProfile>();
		for (SkypeProfileEntity entity : skypeProfileRepository.findBySIPNotNull()) {
			listSkypeProfile.add(entityMapperSkypeProfile.mapToDomain(entity));
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
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findBySIP(sip));
	}

//US005 Mise à jour d'un profil skype
	@Override
	public void update(String sip, SkypeProfile skypeProfileUpdated) {

		// Récupérer le profil Skype à partir de l'identifiant SIP

		SkypeProfileEntity skypeProfileEntityDB = skypeProfileRepository.findBySIP(sip);

		if (skypeProfileEntityDB == null) {
			throw new RuntimeException("Profil skype non trouvé , SIP : " + skypeProfileUpdated.getSIP());
		} else {

			// Mapper le skypeProfil Domaine

			SkypeProfileEntity skypeProfileEntity = entityMapperSkypeProfile.mapToEntity(skypeProfileUpdated);

			// Compléter l'Entity avec l'IdSkypeProfile et l'objet Collaborater

			skypeProfileEntity.setIdSkypeProfile(skypeProfileEntityDB.getIdSkypeProfile());

			skypeProfileEntity.setCollaborater(skypeProfileEntityDB.getCollaborater());

			// Mise à jour du profil Skype

			skypeProfileRepository.save(skypeProfileEntity);

		}

	}

	@Override
	public SkypeProfile findSkypeProfileByCollaborater(Collaborater collaborater) {
		// TODO Auto-generated method stub
		CollaboraterEntity collaboraterEntity = entityMapperCollaborater.mapToEntity(collaborater);
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findByCollaborater(collaboraterEntity));

	}

}
