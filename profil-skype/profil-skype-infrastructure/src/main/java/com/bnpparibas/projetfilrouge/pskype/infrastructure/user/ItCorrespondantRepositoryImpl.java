package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;


/**
 * Dédié au ItCorrespondant
 * Elle assure la correspondance entre les méthodes exposées de la couche domaine et celles da la couche infrastructure
 * liste des méthodes :
 * - Création d'un CIL (US010)
 * - Récupérer la liste des CIL (US007)
 * - Mise à jour des roles du CIL (US008)
 * - Suppression d'un CIL en base (US008)
 * - Recherche dynamique de CIL en fonction de critères (US011)
 * @author Judicaël
 *
 */
@Repository
public class ItCorrespondantRepositoryImpl implements IItCorrespondantDomain {

	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantRepositoryImpl.class);
	
	@Autowired
	private ItCorrespondantEntityMapper entityMapper;
	
	@Autowired
	private CollaboraterEntityMapper collabMapper;
	
	@Autowired
	private IItCorrespondantRepository itCorrespondantRepository;
	
	@Autowired
	private ICollaboraterRepository collaboraterRepository;
		
	/**
	 * US010
	 * Création complète en base d'un ItCorrespondant 
	 * @param ItCorrespondant
	 * @return booléen
	 * 
	 */
	@Override
	public boolean createFull(ItCorrespondant itCorrespondant) {
		logger.debug("create full repository, id annuaire : "+itCorrespondant.getCollaboraterId());
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(itCorrespondant.getCollaboraterId());
		if (entity==null) {
			entity = entityMapper.mapToEntity(itCorrespondant);
			itCorrespondantRepository.save(entity);
			return true;
		}else {
			logger.error("It correspondant "+itCorrespondant.getCollaboraterId()+" existe déjà");
//			throw new RuntimeException("It correspondant "+itCorrespondant.getCollaboraterId()+" existe déjà");
			return false;
		}
	}
	
	/**
	 * US021
	 * Création de l'it correspondant à partif du collaborater
	 * @param ItCorrespondant
	 * @return booléen
	 */
	@Override
	public boolean create(ItCorrespondant itCorrespondant) {
		
		CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(itCorrespondant.getCollaboraterId());
		if (collaboraterEntity ==null) {
			logger.error("Collaborater "+itCorrespondant.getCollaboraterId()+" non trouvé");
			return false;
		}
		ItCorrespondantEntity entity = new ItCorrespondantEntity(collaboraterEntity, itCorrespondant.getCollaboraterId(),itCorrespondant.getPassword());
		entity.setRoles(itCorrespondant.getRoles());
		itCorrespondantRepository.save(entity);
		return true;
	}
	
	/**
	 * US008
	 * Mise à jour des roles du CIL 
	 * Le Set de rôles annule et remplace les rôles existants
	 * @author Judicaël
	 */
	@Override
	public boolean update(String idAnnuaire, Set<RoleTypeEnum> roles) {
		
		logger.debug("Mise à jour du rôle It Correspondant");
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
		if (entity == null) {
//			throw new RuntimeException("Mise à jour impossible, id : "+idAnnuaire+" non trouvé");
			logger.error("Mise à jour impossible, id : "+idAnnuaire+" non trouvé");
			return false;
		}else {
			entity.setRoles(roles);
			itCorrespondantRepository.save(entity);
			return true;
		}
		
	}

	/**
	 * US007
	 * Récupération de l'ensemble des ItCorrespondant tous rôles confondus
	 * @param null
	 * @return List<ItCorrespondant>
	 * 
	 */
	@Override
	public List<ItCorrespondant> findAllItCorrespondant() {
		
		
		List<ItCorrespondant> listItCorrespondant = new ArrayList<ItCorrespondant>();
		for (ItCorrespondantEntity entity : itCorrespondantRepository.findByItCorrespondantIdNotNull()) {
			listItCorrespondant.add(entityMapper.mapToDomain(entity));
		}
		return listItCorrespondant;
	}
	/**
	 * US011
	 * Méthode de recherche de CIL sur la base des critères fournis en entrée.
	 * Cette méthode sur la méthode de recherche privée puis effectue le mapping
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param deskPhone
	 * @param mobilePhone
	 * @param mailAddress
	 * @return List<ItCorrespondant>
	 */
	@Override
	public List<ItCorrespondant> findAllItCorrespondantFilters(String id, String lastName, String firstName, String deskPhone, String mobilePhone, String mailAddress) {
				
		List<ItCorrespondantEntity> listItCorrespondantEntity = findAllItCorrespondantEntityFilters(id,lastName,firstName, deskPhone,mobilePhone,mailAddress);
		List<ItCorrespondant> listItCorrespondant = new ArrayList<ItCorrespondant>();
		for (ItCorrespondantEntity entity :listItCorrespondantEntity) {
			listItCorrespondant.add(entityMapper.mapToDomain(entity));
		}
		
	//	listItCorrespondantEntity = itCorrespondantRepository.findAll(new Specification<ItCorrespondantEntity>());
		return listItCorrespondant;
	}
	/**
	 * US011
	 * Cette méthode privée s'appuie sur le concept de Spécification qui permet de construire des critères de requêtage et des les appliquer
	 * Pour le nom et le prénom, la recherche partielle est acceptée
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param deskPhone
	 * @param mobilePhone
	 * @param mailAddress
	 * @return List<ItCorrespondantEntity>
	 * @author Judicaël
	 */
	private List<ItCorrespondantEntity> findAllItCorrespondantEntityFilters(String id, String lastName, String firstName, String deskPhoneNumber, String mobilePhoneNumber, String mailAdress) {
		
		List<ItCorrespondantEntity> listItCorrespondantEntity = new ArrayList<ItCorrespondantEntity>();
		listItCorrespondantEntity = itCorrespondantRepository.findAll(new Specification<ItCorrespondantEntity>() {
			
			@Override
			public Predicate toPredicate(Root<ItCorrespondantEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (id!= null && id != "") {
					logger.debug("recherche par id "+id);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("collaboraterId"),id));
				}
				//Pour le nom, on permet la recherche partielle.
				//Non sensible à la casse
				if (lastName!= null && lastName != "") {
					logger.debug("recherche par lastName "+ lastName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("lastName")),"%"+lastName.toLowerCase()+"%"));
				}
				//Pour le prénom, on permet la recherche partielle.
				//Non sensible à la casse
				if (firstName!= null && firstName != "") {
					logger.debug("recherche par firstName "+ firstName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("firstName")),"%"+firstName.toLowerCase()+"%"));
				}
				if (deskPhoneNumber!= null && deskPhoneNumber != "") {
					logger.debug("recherche par deskPhoneNumber "+ deskPhoneNumber);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("deskPhoneNumber"),deskPhoneNumber));
				}
				if (mobilePhoneNumber!= null && mobilePhoneNumber != "") {
					logger.debug("recherche par mobilePhone "+ mobilePhoneNumber);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("mobilePhoneNumber"),mobilePhoneNumber));
				}
				if (mailAdress!= null && mailAdress != "") {
					logger.debug("recherche par mailAddress "+ mailAdress);
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("mailAdress"),mailAdress));
				}
				
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}
			
		});
		for (ItCorrespondantEntity entity :listItCorrespondantEntity) {
			logger.debug("nom "+entity.getCollaborater().getLastName());
			logger.debug("prénom "+entity.getCollaborater().getFirstName());
			logger.debug("--------");
		}
		return listItCorrespondantEntity;

	}
	


	/**
	 * @param String id annuaire du CIL
	 * @return l'objet CIL correspondant
	 * @author Judicaël
	 * @version V0.1
	 */
	@Override
	public ItCorrespondant findItCorrespondantByCollaboraterId(String id) {
		
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(id);
		if (entity == null) {
			logger.error("Pas de colaborateur trouvé pour id : "+id);
//			throw new RuntimeException("Pas de colaborateur trouvé pour id : "+id);
			return null;
		}
		else {
			return entityMapper.mapToDomain(entity);
		}

	}

	/**
	 * Suppression d'un CIL en base (US008)
	 * @param ItCorrespondant CIL
	 */
	@Override
	public boolean delete(ItCorrespondant itCorrespondant) {
		
		logger.debug("Suppression it correspondant, id annuaire "+itCorrespondant.getCollaboraterId());
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(itCorrespondant.getCollaboraterId());
		logger.debug("Suppression  entity it correspondant, id "+entity.getIdUser());
		itCorrespondantRepository.delete(entity);
		return true;
	}
	/**
	 * Spring Security : mise à jour du mot de passe encodé
	 * @param String idAnnuaire
	 * @param String newEncryptedPassword
	 */
	@Override
	public boolean updatePassword(String idAnnuaire, String newEncryptedPassword) {
		
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
		entity.setEncryptedPassword(newEncryptedPassword);
		itCorrespondantRepository.save(entity);
		return true;
	}

	@Override
	public boolean createRoleCILtoCollab(String idAnnuaire, Set<RoleTypeEnum> roles, String password) {

		CollaboraterEntity collabEntity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (collabEntity == null) {
			logger.error("Pas de colaborateur trouvé pour id : "+idAnnuaire);
//			throw new RuntimeException("Pas de colaborateur trouvé pour id : "+idAnnuaire);
			return false;
		}
		else {
			ItCorrespondantEntity entityRepo = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
			if (entityRepo != null) {
				logger.error("Un rôle existe déjà pour ce collaborateur : " + idAnnuaire);
//				throw new RuntimeException("Un rôle CIL existe déjà pour ce collaborateur : " + idAnnuaire);
				return false;
			} else {
				ItCorrespondantEntity itCorrespEntity = new ItCorrespondantEntity();
				itCorrespEntity.setCollaborater(collabEntity);
				itCorrespEntity.setEncryptedPassword(password);
				itCorrespEntity.setRoles(roles);
				itCorrespEntity.setItCorrespondantId(idAnnuaire);			
				itCorrespondantRepository.save(itCorrespEntity);
				return true;
			}
		}		
	}
}

