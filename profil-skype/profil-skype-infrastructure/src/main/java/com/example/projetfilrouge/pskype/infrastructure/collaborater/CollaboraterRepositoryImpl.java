package com.example.projetfilrouge.pskype.infrastructure.collaborater;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.ICollaboraterDomain;
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

	private static Logger logger = LoggerFactory.getLogger(CollaboraterRepositoryImpl.class);
	private String attributeCriteria = "collaboraterId";

	@Autowired
	CollaboraterEntityMapper mapperCollab;
	
	@Autowired
	ICollaboraterRepository collaboraterRepository;
	
	@Override
	public boolean create(Collaborater collaborater) {
		CollaboraterEntity entity;
		try{
			entity = collaboraterRepository.save(mapperCollab.mapToEntity(collaborater));
		}catch (Exception e){
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,"Pb lors de la création d'un collaborater");
		}
		return (entity !=null);

	}

	@Override
	public Collaborater findByCollaboraterId(String idAnnuaire) {

		CollaboraterEntity entity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (entity == null) {
			if (logger.isInfoEnabled()){
				String sLogInfo = "Pas de collaborateur pour id :"+idAnnuaire;
				logger.info(sLogInfo);
			}
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
		
		return listCollaborater;
	}

	@Override
	public List<Collaborater> findAllCollaboraterPage(int numberPage, int sizePage, String criteria,
			boolean sortAscending) {
		List<CollaboraterEntity> listEntity;
		boolean sortCriteria = verifyCriteriaAttribute(criteria);

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

		if (verifyCriteriaAttribute(attribute)) {
			attributeCriteria=attribute;
		}
		if (sortAscending) {
			listEntity = findAllCollaboraterByPage(collaborater,PageRequest.of(numberPage,sizePage,Sort.by(attributeCriteria).ascending()));
		}else {
			listEntity = findAllCollaboraterByPage(collaborater,PageRequest.of(numberPage,sizePage,Sort.by(attributeCriteria).descending()));
		}
		
		List<Collaborater> listCollaborater = new ArrayList<Collaborater>();
		for (CollaboraterEntity entity : listEntity) {
			listCollaborater.add(mapperCollab.mapToDomain(entity));
		}

		return listCollaborater;
	}

	@Override
	public Long countCollarater() {
		return collaboraterRepository.count();
	}

	private List<CollaboraterEntity> findAllCollaboraterByPage(Collaborater collaborater, Pageable pageable) {

		

		return collaboraterRepository.findAll(new Specification<CollaboraterEntity>() {
			
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@Override
		public Predicate toPredicate(Root<CollaboraterEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) {

			List<Predicate> predicates = new ArrayList<>();
			if ((collaborater.getCollaboraterId()!=null) && !("".equals(collaborater.getCollaboraterId()))){
				predicates.add(criteriaBuilder.equal(root.get(attributeCriteria),collaborater.getCollaboraterId()));
				logger.info("recherche par collaboraterId");
			}
			if ((collaborater.getFirstNamePerson()!=null) && !("".equals(collaborater.getFirstNamePerson()))) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),"%"+collaborater.getFirstNamePerson().toLowerCase()+"%"));
				logger.info("recherche par firstName ");
			}
			if ((collaborater.getLastNamePerson()!=null) && !("".equals(collaborater.getLastNamePerson()))) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),"%"+collaborater.getLastNamePerson().toLowerCase()+"%"));
				logger.info("recherche par lastName ");
			}
			if ((collaborater.getDeskPhoneNumber()!= null) && !("".equals(collaborater.getDeskPhoneNumber()))){
				logger.info("recherche par deskPhoneNumber ");
				predicates.add(criteriaBuilder.equal(root.get("deskPhoneNumber"),collaborater.getDeskPhoneNumber()));
			}
			if ((collaborater.getMobilePhoneNumber()!= null) && !("".equals(collaborater.getMobilePhoneNumber()))){
				logger.info("recherche par mobilePhone ");
				predicates.add(criteriaBuilder.equal(root.get("mobilePhoneNumber"),collaborater.getMobilePhoneNumber()));
			}
			if ((collaborater.getMailAdress()!= null) && !("".equals(collaborater.getMailAdress()))){
				logger.info("recherche par mailAddress ");
				predicates.add(criteriaBuilder.equal(root.get("mailAdress"),collaborater.getMailAdress()));
			}
			if ((collaborater.getOrgaUnit()!=null) && (collaborater.getOrgaUnit().getOrgaUnityCode()!=null) && !("".equals(collaborater.getOrgaUnit().getOrgaUnityCode()))){
				logger.info("recherche par code UO ");
				predicates.add(criteriaBuilder.equal(root.get("orgaUnit").get("orgaUnityCode"),collaborater.getOrgaUnit().getOrgaUnityCode()));
			}
			if ((collaborater.getOrgaUnit().getOrgaSite()!=null) && (collaborater.getOrgaUnit().getOrgaSite().getSiteCode()!=null) && !("".equals(collaborater.getOrgaUnit().getOrgaSite().getSiteCode()))) {
				logger.info("recherche par code Site ");
				predicates.add(criteriaBuilder.equal(root.get("orgaUnit").get("orgaSite").get("siteCode"),collaborater.getOrgaUnit().getOrgaSite().getSiteCode()));
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}
		},PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort()));

	}


}
