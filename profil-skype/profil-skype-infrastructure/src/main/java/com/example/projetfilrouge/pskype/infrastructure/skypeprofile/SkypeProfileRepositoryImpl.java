package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.skypeprofile.ISkypeProfileDomain;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfile;
import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;

import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntityMapper;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.ICollaboraterRepository;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaExceptionListEnum;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.example.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.example.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;


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
	 * @param skypeProfile skypeProfile
	 * @return 
	 */
	@Override

	public boolean create(SkypeProfile skypeProfile) {

		logger.trace("SkypeProfileRepositoryImpl : create");
		SkypeProfileEntity entity = skypeProfileRepository.findBySIP(skypeProfile.getSIP());
		if (entity == null) {
			entity = entityMapperSkypeProfile.mapToEntity(skypeProfile);
			entity.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
			CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId());
			if (collaboraterEntity == null) {
				// on cherche a créer un profil skype pour un collaborateur qui n'existe pas encore
				// par exemple pour les tests
				skypeProfileRepository.save(entity);
			} else {
				if (skypeProfileRepository.findByCollaborater(collaboraterEntity) == null) {//				entity.setCollaborater(collaboraterRepository.findDistinctByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));
					entity.setCollaborater(collaboraterRepository.findByCollaboraterId(skypeProfile.getCollaborater().getCollaboraterId()));

					skypeProfileRepository.save(entity);
				} else {
					String msg = skypeProfile.getCollaborater().getCollaboraterId() + " a déjà un profil skype";
					logger.error(msg);
					throw new AllReadyExistException(ExceptionListEnum.ALLREADY1, msg);
				}
			}

		} else {
			String msg = "Profil Skype " + skypeProfile.getSIP() + " existe déjà";
			logger.error(msg);
			throw new AllReadyExistException(ExceptionListEnum.ALLREADY2, msg);
		}

		return true;
	}
	

	@Override
	public SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status) {

		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findBySIPAndStatusProfile(sip, status));
		
	}
	

	// US006 Supprimer un profil Skype
	@Override

	public boolean delete(String sip) {

		//Récupérer le profil Skype à partir de l'identifiant SIP
		SkypeProfileEntity skypeProfile = skypeProfileRepository.findBySIP(sip);

		if (skypeProfile == null) {
			String msg = "Profil skype non trouvé , SIP : " + sip;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND10, msg);
		} else {

			//Avant la suppresion du profil Skype, on supprime d'abord les événements correspondant.
			skypeProfileEventRepository.deleteAll(skypeProfileEventRepository.findBySkypeProfileSIP(sip));
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

	//US034 : ajouter la pagination dans la restitution des listes
	@Override
	public List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria,
			boolean sortAscending) {
		
		List<SkypeProfile> listSkypeProfile = new ArrayList<SkypeProfile>();
		String attributeCriteria = "SIP";
		if (verifyCriteriagAttribute(criteria)) {
			attributeCriteria=criteria;
		}
		for (SkypeProfileEntity entity : skypeProfileRepository.findBySIPNotNull(PageRequest.of(numberPage, sizePage,Sort.by(attributeCriteria).ascending()))) {
			listSkypeProfile.add(entityMapperSkypeProfile.mapToDomain(entity));
		}
		return listSkypeProfile;
	}
	
	private boolean verifyCriteriagAttribute(String criteria) {
		
		Field[] fields = SkypeProfileEntity.class.getDeclaredFields();
		 
		for(Field field : fields){
			if (field.getName().equals(criteria)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public SkypeProfile findSkypeProfileBySip(String sip) {
		
		SkypeProfileEntity entity = skypeProfileRepository.findBySIP(sip);
		
		if (entity == null) {
			return null;
		}
		return entityMapperSkypeProfile.mapToDomain(entity);
	}


//US005 Mise à jour d'un profil skype
	@Override
	public boolean update(SkypeProfile skypeProfileUpdated) {

		// Récupérer le SIP à partir de l'Id collaborater
		SkypeProfileEntity sp = skypeProfileRepository
				.getSkypeProfilByIdCollab(skypeProfileUpdated.getCollaborater().getCollaboraterId());

		// Récupérer le profil Skype en base de données à partir de l'identifiant SIP
		SkypeProfileEntity skypeProfileEntityDB = skypeProfileRepository.findBySIP(sp.getSIP());

		if (skypeProfileEntityDB == null) {
			String msg = "Profil skype non trouvé , SIP : " + skypeProfileUpdated.getSIP();
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND2, msg);

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
		
		CollaboraterEntity collaboraterEntity = entityMapperCollaborater.mapToEntity(collaborater);
		return entityMapperSkypeProfile.mapToDomain(skypeProfileRepository.findByCollaborater(collaboraterEntity)) ;
	}
 
	@Override
	public SkypeProfile findSkypeProfileByIdCollab(String idAnnuaire) {
		
		SkypeProfileEntity entity = skypeProfileRepository.getSkypeProfilByIdCollab(idAnnuaire);
		if (entity == null) {
			return null;
		}else {
			return entityMapperSkypeProfile.mapToDomain(entity);
		}
	}

	@Override
	public List<SkypeProfile> findAllSkypeProfileFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
			String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
			String orgaUnityCode, String siteCode, String lastName, String firstName, Date expirationDate, String collaboraterId) {

		List<SkypeProfileEntity> profilEntity = findAllSkypeProfileFilter(enterpriseVoiceEnabled, voicePolicy, dialPlan, samAccountName, exUmEnabled, exchUser, statusProfile,
				orgaUnityCode, siteCode, lastName, firstName, expirationDate, collaboraterId);
		
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
	private List<SkypeProfileEntity> findAllSkypeProfileFilter(Boolean enterpriseVoiceEnabled, String voicePolicyWithVoiceEnabled,
			String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
			String orgaUnityCode, String siteCode, String lastName, String firstName, Date expirationDate, String collaboraterId) {

		return skypeProfileRepository.findAll(new Specification<SkypeProfileEntity>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SkypeProfileEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<>();
				if (voicePolicyWithVoiceEnabled.indexOf("voiceEnabled=&") == -1) {
					logger.debug("recherche par enterpriseVoiceEnabled ");
					predicates.add(criteriaBuilder.equal(root.get("enterpriseVoiceEnabled"),enterpriseVoiceEnabled));
				}
				String voicePolicy = voicePolicyWithVoiceEnabled.substring(voicePolicyWithVoiceEnabled.indexOf("&")+1);
				if (voicePolicy != null && !("".equals(voicePolicy))) {
					logger.debug("recherche par voicePolicy ");
					predicates.add(criteriaBuilder.equal(root.get("voicePolicy"),voicePolicy));
				}
				
				if (dialPlan != null && !("".equals(dialPlan))) {
					logger.debug("recherche par dialPlan ");
					predicates.add(criteriaBuilder.equal(root.get("dialPlan"),dialPlan));
				}				
				
				if (samAccountName != null && !("".equals(samAccountName))) {
					logger.debug("recherche par samAccountName ");
					predicates.add(criteriaBuilder.equal(root.get("samAccountName"),samAccountName));
				}	

//				if (exUmEnabled != null) {
//					logger.debug("recherche par exUmEnabled ");
//					predicates.add(criteriaBuilder.equal(root.get("exUmEnabled"),exUmEnabled));
//				}
				
				if (exchUser != null && !("".equals(exchUser))) {
					logger.debug("recherche par exchUser ");
					predicates.add(criteriaBuilder.equal(root.get("exchUser"),exchUser));
				}
				
				if (statusProfile != null) {
					logger.debug("recherche par statusProfile ");
					predicates.add(criteriaBuilder.equal(root.get("statusProfile"),statusProfile));
				}
				
				if (orgaUnityCode != null && !("".equals(orgaUnityCode))) {
					logger.debug("recherche par orgaUnityCode ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaUnityCode"),orgaUnityCode));
				}
				
				if (siteCode != null && !("".equals(siteCode))) {
					logger.debug("recherche par siteCode ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaSite").get("siteCode"),siteCode));
				}
				if (lastName != null && !("".equals(lastName))) {
					logger.debug("recherche par lastName "+lastName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("lastName")),"%"+lastName.toLowerCase()+"%"));
				}

				if (firstName != null && !("".equals(firstName))) {
					logger.debug("recherche par firstName "+firstName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("firstName")),"%"+firstName.toLowerCase()+"%"));
				}
				if (expirationDate!= null  && !("".equals(expirationDate))) {
					logger.debug("recherche par expirationDate "+expirationDate);
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expirationDate"),expirationDate));
				}
				if (collaboraterId != null && !("".equals(collaboraterId))) {
					logger.debug("recherche par collaboraterId "+collaboraterId);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("collaboraterId"),collaboraterId));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

		});

	}


	@Override
	public List<SkypeProfile> findAllSkypeProfileFiltersPage(SkypeProfile profilDom, int numberPage, int sizePage,
			String criteria, boolean sortAscending) {

		logger.info("Profil Dom Infra"+profilDom.toString());
		List<SkypeProfileEntity> profilEntity;
		String sortCriteria="statusProfile";
		if(verifyCriteriaAttribute(criteria)) {
			sortCriteria=criteria;
		}
		if (sortAscending) {
			profilEntity = findAllSkypeProfilByPage(profilDom.isEnterpriseVoiceEnabled(),profilDom.getVoicePolicy(),profilDom.getDialPlan(),profilDom.getSamAccountName(),profilDom.isExUmEnabled(),profilDom.getExchUser(),profilDom.getStatusProfile(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaUnityCode(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode(),
					profilDom.getCollaborater().getFirstNamePerson(),
					profilDom.getCollaborater().getLastNamePerson(),
					profilDom.getExpirationDate(),
					profilDom.getCollaborater().getCollaboraterId(),
					PageRequest.of(numberPage,sizePage,Sort.by(sortCriteria).ascending()));
		}else {
			profilEntity = findAllSkypeProfilByPage(profilDom.isEnterpriseVoiceEnabled(),profilDom.getVoicePolicy(),profilDom.getDialPlan(),profilDom.getSamAccountName(),profilDom.isExUmEnabled(),profilDom.getExchUser(),profilDom.getStatusProfile(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaUnityCode(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode(),
					profilDom.getCollaborater().getFirstNamePerson(),
					profilDom.getCollaborater().getLastNamePerson(),
					profilDom.getExpirationDate(),
					profilDom.getCollaborater().getCollaboraterId(),
					PageRequest.of(numberPage,sizePage,Sort.by(sortCriteria).descending()));
		
		}
		return entityMapperSkypeProfile.mapToDomainList(profilEntity);
	}
	
	private List<SkypeProfileEntity> findAllSkypeProfilByPage(Boolean enterpriseVoiceEnabled, String voicePolicyWithVoiceEnabled,
															  String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
															  String orgaUnityCode, String siteCode, String firstName, String lastName, Date expirationDate, String collaboraterId,
															  Pageable pageable) {

		logger.info("Statut profil :"+statusProfile);
		logger.info("collaboraterId: "+collaboraterId);
		return skypeProfileRepository.findAll(new Specification<SkypeProfileEntity>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SkypeProfileEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<>();
				if (voicePolicyWithVoiceEnabled.indexOf("voiceEnabled=&") ==-1) {
					logger.info("recherche par enterpriseVoiceEnabled ");
					predicates.add(criteriaBuilder.equal(root.get("enterpriseVoiceEnabled"),enterpriseVoiceEnabled));
				}

				String voicePolicy = voicePolicyWithVoiceEnabled.substring(voicePolicyWithVoiceEnabled.indexOf("&")+1);
				if (voicePolicy != null && !("".equals(voicePolicy))) {
					logger.info("recherche par voicePolicy");
					predicates.add(criteriaBuilder.equal(root.get("voicePolicy"),voicePolicy));
				}
				
				if (dialPlan != null && !("".equals(dialPlan))) {
					logger.info("recherche par dialPlan ");
					predicates.add(criteriaBuilder.equal(root.get("dialPlan"),dialPlan));
				}				
				
				if (samAccountName != null && !("".equals(samAccountName))) {
					logger.info("recherche par samAccountName ");
					predicates.add(criteriaBuilder.equal(root.get("samAccountName"),samAccountName));
				}	

//				if (exUmEnabled != null) {
//					logger.debug("recherche par exUmEnabled ");
//					predicates.add(criteriaBuilder.equal(root.get("exUmEnabled"),exUmEnabled));
//				}
				
				if (exchUser != null && !("".equals(exchUser))) {
					logger.info("recherche par exchUser ");
					predicates.add(criteriaBuilder.equal(root.get("exchUser"),exchUser));
				}
				
				if (statusProfile != null) {
					logger.info("recherche par statusProfile ");
					predicates.add(criteriaBuilder.equal(root.get("statusProfile"),statusProfile));
				}

				if (lastName != null && !("".equals(lastName))) {
					logger.info("recherche par lastName "+lastName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("lastName")),"%"+lastName.toLowerCase()+"%"));
				}

				if (firstName != null && !("".equals(firstName))) {
					logger.info("recherche par firstName ");
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("firstName")),"%"+firstName.toLowerCase()+"%"));
				}

				if (orgaUnityCode != null && !("".equals(orgaUnityCode))) {
					logger.info("recherche par orgaUnityCode "+orgaUnityCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaUnityCode"),orgaUnityCode));
				}
				
				if (siteCode != null && !("".equals(siteCode))) {
					logger.info("recherche par siteCode "+siteCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaSite").get("siteCode"),siteCode));
				}
				if (expirationDate!= null  && !("".equals(expirationDate))) {
					logger.info("recherche par expirationDate "+expirationDate);
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expirationDate"),expirationDate));
				}
				if (collaboraterId != null && !("".equals(collaboraterId))) {
					logger.info("recherche par collaboraterId "+collaboraterId);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("collaboraterId"),collaboraterId));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

		},PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort())).getContent();

	}

	
	private boolean verifyCriteriaAttribute(String criteria) {
		
		Field[] fields = SkypeProfileEntity.class.getDeclaredFields();
		 
		//Par soucis de simplification, je ne descends pas l'arborescence les attributs des objets inclus 
		if (("orgaUnityCode").equals(criteria)){
			return true;
		}
		if (("siteCode").equals(criteria)){
			return true;
		}
		for(Field field : fields){
			if (field.getName().equals(criteria)) {
				return true;
			}
		}
		return false;
	}
	
	//US034 : ajouter la pagination dans la restitution des listes
	@Override
	public Long countSkypeProfile() {
		
		return skypeProfileRepository.count();
	}

}
