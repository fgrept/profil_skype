package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;

//import jdk.internal.org.jline.utils.Log;

import java.lang.reflect.*;

/**
 * Dédié au collaborateur uniquement
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - creation du collaborateur
 * - récupération du collaborateur à partir de l'id annuaire
 * @author Judicaël
 *
 */
@Repository
public class CollaboraterRepositoryImpl implements ICollaboraterDomain {

	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantRepositoryImpl.class);
	
	@Autowired
	CollaboraterEntityMapper mapperCollab;
	
	@Autowired
	ICollaboraterRepository collaboraterRepository;
	
	@Override
	public boolean create(Collaborater collaborater) {
		
		CollaboraterEntity entity = collaboraterRepository.save(mapperCollab.mapToEntity(collaborater));
		if (entity !=null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Collaborater findByCollaboraterId(String idAnnuaire) {
		// TODO Auto-generated method stub
//		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
//		CollaboraterEntity entity = collaboraterRepository.findDistinctByCollaboraterId(idAnnuaire);
		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (entity == null) {
			logger.info("Pas de collaborateur pour id :"+logger);
			return null;
		}else {
			return mapperCollab.mapToDomain(entity);
		}
	}

	@Override
	public List<Collaborater> findAllCollaborater() {
		
		List<Collaborater> listCollaborater = new ArrayList<Collaborater>();
		
		for (CollaboraterEntity entity : collaboraterRepository.findByCollaboraterIdNotNull()) {
			listCollaborater.add(mapperCollab.mapToDomain(entity));
		}
		
	//	List<CollaboraterEntity> list = collaboraterRepositoryPage.findAll(PageRequest.of(0, 10)).get;
		List<CollaboraterEntity> list = collaboraterRepository.findByCollaboraterIdNotNull(PageRequest.of(0, 5,Sort.by("CollaboraterId").descending()));
		for (CollaboraterEntity entity : list) {
			logger.info(entity.toString());
		}
		
		Field[] fields = new CollaboraterEntity().getClass().getDeclaredFields();
 
		for(Field f : fields){
			logger.info(f.getName());
		}
		
		return listCollaborater;
	}

	@Override
	public List<Collaborater> findAllCollaboraterPage(int numberPage, int sizePage, String criteria,
			boolean sortAscending) {
		List<CollaboraterEntity> listEntity;
		boolean sortCriteria = verifyCriteriaAttribute(criteria);
		String attributeCriteria = "collaboraterId";
		if (sortCriteria) {
			attributeCriteria=criteria;
		}
		if(sortAscending) {
			listEntity = collaboraterRepository.findByCollaboraterIdNotNull(PageRequest.of(numberPage, sizePage,Sort.by(attributeCriteria).ascending()));
		}else {
			listEntity = collaboraterRepository.findByCollaboraterIdNotNull(PageRequest.of(numberPage, sizePage,Sort.by(attributeCriteria).descending()));
		}
		List<Collaborater> listCollaborater = new ArrayList<Collaborater>();
		
		for (CollaboraterEntity entity : listEntity) {
			listCollaborater.add(mapperCollab.mapToDomain(entity));
		}

		return listCollaborater;
	}

	
	private boolean verifyCriteriaAttribute(String criteria) {
		
		Field[] fields = CollaboraterEntity.class.getDeclaredFields();
		 
		for(Field field : fields){
			if (field.getName().equals(criteria)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Collaborater> findAllCollaboraterCriteriaPage(Collaborater collaborater, int numberPage, int sizePage,
			String attribute, boolean sortAscending) {
		
		List<CollaboraterEntity> listEntity;
		String attributeCriteria = "collaboraterId";
		if (verifyCriteriaAttribute(attribute)) {
			attributeCriteria=attribute;
		}
		if (sortAscending) {
			listEntity = findAllCollaboraterEntityCriteriaPage(collaborater,PageRequest.of(numberPage,sizePage,Sort.by(attributeCriteria).ascending()));
		}else {
			listEntity = findAllCollaboraterEntityCriteriaPage(collaborater,PageRequest.of(numberPage,sizePage,Sort.by(attributeCriteria).descending()));
		}
		
		List<Collaborater> listCollaborater = new ArrayList<Collaborater>();
		for (CollaboraterEntity entity : listEntity) {
			listCollaborater.add(mapperCollab.mapToDomain(entity));
		}

		return listCollaborater;
	}

	private List<CollaboraterEntity> findAllCollaboraterEntityCriteriaPage(Collaborater collaborater, Pageable pageable) {
		// TODO Auto-generated method stub
		
		List<CollaboraterEntity> listCollaborater = new ArrayList<CollaboraterEntity>();
		
		listCollaborater = collaboraterRepository.findAll(new Specification<CollaboraterEntity>() {
			
		@Override
		public Predicate toPredicate(Root<CollaboraterEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) {
			
			List<Predicate> predicates = new ArrayList<>();
			if(collaborater.getCollaboraterId()!=null) {
				predicates.add(criteriaBuilder.equal(root.get("collaboraterId"),collaborater.getCollaboraterId()));
				logger.info("recherche par collaboraterId");
			}
			if(collaborater.getFirstNamePerson()!=null) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),"%"+collaborater.getFirstNamePerson().toLowerCase()+"%"));
				logger.info("recherche par firstName "+collaborater.getFirstNamePerson());
			}
			if (collaborater.getLastNamePerson()!=null){
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),"%"+collaborater.getLastNamePerson().toLowerCase()+"%"));
				logger.info("recherche par lastName "+collaborater.getLastNamePerson());
			}
			if (collaborater.getDeskPhoneNumber()!= null) {
				logger.info("recherche par deskPhoneNumber "+ collaborater.getDeskPhoneNumber());
				predicates.add(criteriaBuilder.equal(root.get("deskPhoneNumber"),collaborater.getDeskPhoneNumber()));
			}
			if (collaborater.getMobilePhoneNumber()!= null) {
				logger.info("recherche par mobilePhone "+ collaborater.getMobilePhoneNumber());
				predicates.add(criteriaBuilder.equal(root.get("mobilePhoneNumber"),collaborater.getMobilePhoneNumber()));
			}
			if (collaborater.getMailAdress()!= null) {
				logger.info("recherche par mailAddress "+ collaborater.getMailAdress());
				predicates.add(criteriaBuilder.equal(root.get("mailAdress"),collaborater.getMailAdress()));
			}
			if ((collaborater.getOrgaUnit()!=null) && (collaborater.getOrgaUnit().getOrgaUnityCode()!=null)){
				logger.info("recherche par code UO "+ collaborater.getOrgaUnit().getOrgaUnityCode());
				predicates.add(criteriaBuilder.equal(root.get("orgaUnit").get("orgaUnityCode"),collaborater.getOrgaUnit().getOrgaUnityCode()));
			}
			if ((collaborater.getOrgaUnit().getOrgaSite()!=null) && ((collaborater.getOrgaUnit().getOrgaSite().getSiteCode()!=null))) {
				logger.info("recherche par code Site "+ collaborater.getOrgaUnit().getOrgaSite().getSiteCode());
				predicates.add(criteriaBuilder.equal(root.get("orgaUnit").get("orgaSite").get("siteCode"),collaborater.getOrgaUnit().getOrgaSite().getSiteCode()));
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}
		},PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort()));
		return listCollaborater;
	}


}
