package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.NotFoundException;
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
			entity.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
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

					skypeProfileRepository.save(entity);
					return true;
				}
				else {
					String msg = skypeProfile.getCollaborater().getCollaboraterId()+" a déjà un profil skype";
					logger.error(msg);
					throw new AllReadyExistException(ExceptionListEnum.ALLREADY1, msg);
				}
			}

		}else {
			String msg = "Profil Skype "+skypeProfile.getSIP()+" existe déjà";
			logger.error(msg);
			throw new AllReadyExistException(ExceptionListEnum.ALLREADY2, msg);
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
			String msg = "Profil skype non trouvé , SIP : "+sip;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND1, msg);
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
		
		Field[] fields = new SkypeProfileEntity().getClass().getDeclaredFields();
		 
		for(Field field : fields){
			if (field.getName().equals(criteria)) {
				return true;
			}
		}
		return false;
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
					logger.debug("recherche par enterpriseVoiceEnabled "+enterpriseVoiceEnabled);
					predicates.add(criteriaBuilder.equal(root.get("enterpriseVoiceEnabled"),enterpriseVoiceEnabled));
				}
				
				if (voicePolicy != null && voicePolicy != "") {
					logger.debug("recherche par voicePolicy "+voicePolicy);
					predicates.add(criteriaBuilder.equal(root.get("voicePolicy"),voicePolicy));
				}
				
				if (dialPlan != null && dialPlan != "") {
					logger.debug("recherche par dialPlan "+dialPlan);
					predicates.add(criteriaBuilder.equal(root.get("dialPlan"),dialPlan));
				}				
				
				if (samAccountName != null && samAccountName != "") {
					logger.debug("recherche par samAccountName "+samAccountName);
					predicates.add(criteriaBuilder.equal(root.get("samAccountName"),samAccountName));
				}	

				if (exUmEnabled != null) {
					logger.debug("recherche par exUmEnabled "+exUmEnabled);
					predicates.add(criteriaBuilder.equal(root.get("exUmEnabled"),exUmEnabled));
				}
				
				if (exchUser != null && exchUser != "") {
					logger.debug("recherche par exchUser "+exchUser);
					predicates.add(criteriaBuilder.equal(root.get("exchUser"),exchUser));
				}
				
				if (statusProfile != null) {
					logger.debug("recherche par statusProfile "+statusProfile);
					predicates.add(criteriaBuilder.equal(root.get("statusProfile"),statusProfile));
				}
				
				if (orgaUnityCode != null) {
					logger.debug("recherche par orgaUnityCode "+orgaUnityCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaUnityCode"),orgaUnityCode));
				}
				
				if (siteCode != null) {
					logger.debug("recherche par siteCode "+siteCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaSite").get("siteCode"),siteCode));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

		});
		
		return profilEntity;
	}


	@Override
	public List<SkypeProfile> findAllSkypeProfileFiltersPage(SkypeProfile profilDom, int numberPage, int sizePage,
			String criteria, boolean sortAscending) {

		List<SkypeProfileEntity> profilEntity = new ArrayList<SkypeProfileEntity>();
		String sortCriteria="statusProfile";
		if(verifyCriteriaAttribute(criteria)) {
			sortCriteria=criteria;
		}
		if (sortAscending) {
			profilEntity=findAllSkypeProfileEntityFiltersPage(profilDom.isEnterpriseVoiceEnabled(),profilDom.getVoicePolicy(),profilDom.getDialPlan(),profilDom.getSamAccountName(),profilDom.isExUmEnabled(),profilDom.getExchUser(),profilDom.getStatusProfile(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaUnityCode(),profilDom.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode(),PageRequest.of(numberPage,sizePage,Sort.by(sortCriteria).ascending()));
		}else {
			profilEntity=findAllSkypeProfileEntityFiltersPage(profilDom.isEnterpriseVoiceEnabled(),profilDom.getVoicePolicy(),profilDom.getDialPlan(),profilDom.getSamAccountName(),profilDom.isExUmEnabled(),profilDom.getExchUser(),profilDom.getStatusProfile(),
					profilDom.getCollaborater().getOrgaUnit().getOrgaUnityCode(),profilDom.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode(),PageRequest.of(numberPage,sizePage,Sort.by(sortCriteria).descending()));
		
		}
		return entityMapperSkypeProfile.mapToDomainList(profilEntity);
	}
	
	private List<SkypeProfileEntity> findAllSkypeProfileEntityFiltersPage(Boolean enterpriseVoiceEnabled, String voicePolicy,
			String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser, StatusSkypeProfileEnum statusProfile,
			String orgaUnityCode, String siteCode, Pageable pageable) {
		
		List<SkypeProfileEntity> profilEntity = new ArrayList<SkypeProfileEntity>();
		profilEntity = skypeProfileRepository.findAll(new Specification<SkypeProfileEntity>() {
			
			@Override
			public Predicate toPredicate(Root<SkypeProfileEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<>();
				if (enterpriseVoiceEnabled != null) {
					logger.debug("recherche par enterpriseVoiceEnabled "+enterpriseVoiceEnabled);
					predicates.add(criteriaBuilder.equal(root.get("enterpriseVoiceEnabled"),enterpriseVoiceEnabled));
				}
				
				if (voicePolicy != null && voicePolicy != "") {
					logger.debug("recherche par voicePolicy "+voicePolicy);
					predicates.add(criteriaBuilder.equal(root.get("voicePolicy"),voicePolicy));
				}
				
				if (dialPlan != null && dialPlan != "") {
					logger.debug("recherche par dialPlan "+dialPlan);
					predicates.add(criteriaBuilder.equal(root.get("dialPlan"),dialPlan));
				}				
				
				if (samAccountName != null && samAccountName != "") {
					logger.debug("recherche par samAccountName "+samAccountName);
					predicates.add(criteriaBuilder.equal(root.get("samAccountName"),samAccountName));
				}	

				if (exUmEnabled != null) {
					logger.debug("recherche par exUmEnabled "+exUmEnabled);
					predicates.add(criteriaBuilder.equal(root.get("exUmEnabled"),exUmEnabled));
				}
				
				if (exchUser != null && exchUser != "") {
					logger.debug("recherche par exchUser "+exchUser);
					predicates.add(criteriaBuilder.equal(root.get("exchUser"),exchUser));
				}
				
				if (statusProfile != null) {
					logger.debug("recherche par statusProfile "+statusProfile);
					predicates.add(criteriaBuilder.equal(root.get("statusProfile"),statusProfile));
				}
				
				if (orgaUnityCode != null) {
					logger.debug("recherche par orgaUnityCode "+orgaUnityCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaUnityCode"),orgaUnityCode));
				}
				
				if (siteCode != null) {
					logger.debug("recherche par siteCode "+siteCode);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("orgaUnit").get("orgaSite").get("siteCode"),siteCode));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

//		},PageRequest.of(numberPage, sizePage,Sort.by(sortCriteria).ascending())).getContent();
		},PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort())).getContent();
			
		return profilEntity;
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
