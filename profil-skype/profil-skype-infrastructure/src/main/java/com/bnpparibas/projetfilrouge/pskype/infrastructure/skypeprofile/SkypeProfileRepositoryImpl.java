package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileRepositoryImpl.class);

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
	 * @return 
	 */
	@Override

	public boolean create(SkypeProfile skypeProfile) {
	
		logger.trace("SkypeProfileRepositoryImpl : create");
		SkypeProfileEntity entity = skypeProfileRepository.findBySIP(skypeProfile.getSIP());
		if (entity==null) {
			entity = entityMapperSkypeProfile.mapToEntity(skypeProfile);
//			System.out.println("SkypeProfileRepositoryImpl : après mapping");
			entity.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
//			System.out.println("SkypeProfileRepositoryImpl : avant récupération collaborateur");
//			CollaboraterEntity collaboraterEntity = collaboraterRepository.findDistinctByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId());
			CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId());
			if (collaboraterEntity ==null) { 
				// on cherche a créer un profil skype pour un collaborateur qui n'existe pas encore
				// par exemple pour les tests
				skypeProfileRepository.save(entity);
				return true;
			}
			else {
				if (skypeProfileRepository.findByCollaborater(collaboraterEntity)==null) {
//				entity.setCollaborater(collaboraterRepository.findDistinctByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));
					entity.setCollaborater(collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));
					System.out.println("SkypeProfileRepositoryImpl : avant sauvegarde");
					skypeProfileRepository.save(entity);
					return true;
				}
				else {
					logger.error(skypeProfile.getCollaborater().getCollaboraterId()+" a déjà un profil skype");
					return false;
				}
			}

		}else {
			logger.error("Profil Skype "+skypeProfile.getSIP()+" existe déjà");
			return false;
		}
	}
	

	@Override
	public SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status) {
		// TODO Auto-generated method stub
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findBySIPAndStatusProfile(sip, status));
		
	}
	

	// US006 Supprimer un profil Skype
	@Override

	public boolean delete(String sip) {
		
		//Récupérer le profil Skype à partir de l'identifiant SIP		
		SkypeProfileEntity skypeProfile = skypeProfileRepository.findBySIP(sip);

		if (skypeProfile == null) {
			logger.error("Profil skype non trouvé , SIP : "+sip);
			return false;
		} else {

			//Avant la suppresion du profil Skype, on supprime d'abord les événements correspondant.		
			skypeProfileEventRepository.deleteAll(skypeProfileEventRepository.findBySkypeProfile(skypeProfile));
			skypeProfileRepository.delete(skypeProfile);
			return true;

		}
	

	}

	@Override
	public List<SkypeProfile> findAllSkypeProfile() {
		
		List<SkypeProfile> listSkypeProfile = new ArrayList<SkypeProfile>();
		for (SkypeProfileEntity entity : skypeProfileRepository.findBySIPNotNull()) {
			listSkypeProfile.add(entityMapperSkypeProfile.mapToDomain(entity));
		}
		return listSkypeProfile;
	}

	@Override
	public SkypeProfile findSkypeProfileBySip(String sip) {
		
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findBySIP(sip));
	}


//US005 Mise à jour d'un profil skype
	@Override
	public boolean update(SkypeProfile skypeProfileUpdated) {
			
		// Récupérer le SIP à partir de l'Id collaborater
		
		SkypeProfileEntity sp = skypeProfileRepository
				                .getSkypeProfilByIdCollab(skypeProfileUpdated.getCollaborater().getCollaboraterId()) ;
				
		// Récupérer le profil Skype en base de données à partir de l'identifiant SIP
		
		SkypeProfileEntity skypeProfileEntityDB = skypeProfileRepository.findBySIP(sp.getSIP());

		if (skypeProfileEntityDB == null) {
			logger.error("Profil skype non trouvé , SIP : " + skypeProfileUpdated.getSIP());
			return false;
			
		} else {

			// Mapper le skypeProfil Domaine

			SkypeProfileEntity skypeProfileEntity = entityMapperSkypeProfile.mapToEntity(skypeProfileUpdated);

			// Compléter l'Entity avec l'IdSkypeProfile et l'objet Collaborater

			skypeProfileEntity.setIdSkypeProfile(skypeProfileEntityDB.getIdSkypeProfile());

			skypeProfileEntity.setCollaborater(skypeProfileEntityDB.getCollaborater());

			// Mise à jour du profil Skype

			skypeProfileRepository.save(skypeProfileEntity);

			return true;
		}
		

	}

	@Override
	public SkypeProfile findSkypeProfileByCollaborater(Collaborater collaborater) {
		// TODO Auto-generated method stub
		CollaboraterEntity collaboraterEntity = entityMapperCollaborater.mapToEntity(collaborater);
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findByCollaborater(collaboraterEntity)) ;
	}
 
	@Override
	public SkypeProfile findSkypeProfileByIdCollab(String idAnnuaire) {
		
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.getSkypeProfilByIdCollab(idAnnuaire));
	}

	@Override
	public List<SkypeProfile> findAllSkypeProfileFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
			String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
			String orgaUnityCode, String siteCode) {
		
		List<SkypeProfileEntity> profilEntity = new ArrayList<SkypeProfileEntity>();
		profilEntity = findAllSkypeProfileEntityFilters(enterpriseVoiceEnabled, voicePolicy, dialPlan, samAccountName, exUmEnabled, exchUser, statusProfile,
				orgaUnityCode, siteCode);
		
		return entityMapperSkypeProfile.mapToDomainList(profilEntity);
	}

	/**
	 * US004 Méthode privée basée sur les spécifications JPA pour ajouter des critères variable de requêtes
	 * 
	 * @param enterpriseVoiceEnabled
	 * @param voicePolicy
	 * @param dialPlan
	 * @param samAccountName
	 * @param exUmEnabled
	 * @param exchUser
	 * @return
	 */
	private List<SkypeProfileEntity> findAllSkypeProfileEntityFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
			String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
			String orgaUnityCode, String siteCode) {
		
		List<SkypeProfileEntity> profilEntity = new ArrayList<SkypeProfileEntity>();
		profilEntity = skypeProfileRepository.findAll(new Specification<SkypeProfileEntity>() {
			
			@Override
			public Predicate toPredicate(Root<SkypeProfileEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<>();
				if (enterpriseVoiceEnabled != null) {
					System.out.println("recherche par enterpriseVoiceEnabled "+enterpriseVoiceEnabled);
					predicates.add(criteriaBuilder.equal(root.get("enterpriseVoiceEnabled"),enterpriseVoiceEnabled));
				}
				
				if (voicePolicy != null && voicePolicy != "") {
					System.out.println("recherche par voicePolicy "+voicePolicy);
					predicates.add(criteriaBuilder.equal(root.get("voicePolicy"),voicePolicy));
				}
				
				if (dialPlan != null && dialPlan != "") {
					System.out.println("recherche par dialPlan "+dialPlan);
					predicates.add(criteriaBuilder.equal(root.get("dialPlan"),dialPlan));
				}				
				
				if (samAccountName != null && samAccountName != "") {
					System.out.println("recherche par samAccountName "+samAccountName);
					predicates.add(criteriaBuilder.equal(root.get("samAccountName"),samAccountName));
				}	

				if (exUmEnabled != null) {
					System.out.println("recherche par exUmEnabled "+exUmEnabled);
					predicates.add(criteriaBuilder.equal(root.get("exUmEnabled"),exUmEnabled));
				}
				
				if (exchUser != null && exchUser != "") {
					System.out.println("recherche par exchUser "+exchUser);
					predicates.add(criteriaBuilder.equal(root.get("exchUser"),exchUser));
				}
				
				if (statusProfile != null) {
					System.out.println("recherche par statusProfile "+statusProfile);
					predicates.add(criteriaBuilder.equal(root.get("statusProfile"),statusProfile));
				}
				
				if (orgaUnityCode != null) {
					System.out.println("recherche par orgaUnityCode "+orgaUnityCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaUnityCode"),orgaUnityCode));
				}
				
				if (siteCode != null) {
					System.out.println("recherche par siteCode "+siteCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaSite").get("siteCode"),siteCode));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

		});
		
		return profilEntity;
	}

}
