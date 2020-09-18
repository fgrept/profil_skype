package com.example.projetfilrouge.pskype.infrastructure.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.projetfilrouge.pskype.domain.user.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.ICollaboraterRepository;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaExceptionListEnum;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


import com.example.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.example.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;


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
	private IItCorrespondantRepository itCorrespondantRepository;
	
	@Autowired
	private ICollaboraterRepository collaboraterRepository;
		
	/**
	 * US010
	 * Création complète en base d'un ItCorrespondant 
	 * @param itCorrespondant
	 * @return booléen
	 * 
	 */
	@Override
	public boolean createFull(ItCorrespondant itCorrespondant) {

		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(itCorrespondant.getCollaboraterId());
		if (entity==null) {
			entity = entityMapper.mapToEntity(itCorrespondant);
			try {
				itCorrespondantRepository.save(entity);
			}catch (Exception e){
				throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,"Pb JPA lors de la création d'un utilisateur");
			}
			return true;
		}else {
			if (logger.isErrorEnabled()){
				String sLogError = "It correspondant "+itCorrespondant.getCollaboraterId()+" existe déjà";
				logger.error(sLogError);
			}

			return false;
		}
	}
	
	/**
	 * US021
	 * Création de l'it correspondant à partif du collaborater
	 * @param itCorrespondant
	 * @return booléen
	 */
	@Override
	public boolean create(ItCorrespondant itCorrespondant) {

		try {
			CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(itCorrespondant.getCollaboraterId());
			if (collaboraterEntity == null) {
				if (logger.isErrorEnabled()) {
					String sLogError = "Collaborater " + itCorrespondant.getCollaboraterId() + " non trouvé";
					logger.error(sLogError);
				}

				return false;
			}
			ItCorrespondantEntity entity = new ItCorrespondantEntity(collaboraterEntity, itCorrespondant.getCollaboraterId(), itCorrespondant.getPassword());
			entity.setRoles(itCorrespondant.getRoles());
			itCorrespondantRepository.save(entity);
		}catch (Exception e){
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,"Pb JPA lors de la création d'un utilisateur");
		}
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

		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
		if (entity == null) {
			String msg = "Mise à jour impossible, id : " + idAnnuaire + " non trouvé";
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND6, msg);
		} else {
			entity.setRoles(roles);
			itCorrespondantRepository.save(entity);
			return true;
		}
	}

	/**
	 * US007
	 * Récupération de l'ensemble des ItCorrespondant tous rôles confondus
	 * @param
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
		
		return listItCorrespondant;
	}
	/**
	 * US011
	 * Cette méthode privée s'appuie sur le concept de Spécification qui permet de construire des critères de requêtage et des les appliquer
	 * Pour le nom et le prénom, la recherche partielle est acceptée
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param deskPhoneNumber
	 * @param mobilePhoneNumber
	 * @param mailAdress
	 * @return List<ItCorrespondantEntity>
	 * @author Judicaël
	 */
	private List<ItCorrespondantEntity> findAllItCorrespondantEntityFilters(String id, String lastName, String firstName, String deskPhoneNumber, String mobilePhoneNumber, String mailAdress) {
		

		return itCorrespondantRepository.findAll(new Specification<ItCorrespondantEntity>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ItCorrespondantEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (id!= null && !("".equals(id))) {
					logger.debug("recherche par id ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("collaboraterId"),id));
				}
				//Pour le nom, on permet la recherche partielle.
				//Non sensible à la casse
				if (lastName!= null && !("".equals(lastName))) {
					logger.debug("recherche par lastName ");
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("lastName")),"%"+lastName.toLowerCase()+"%"));
				}
				//Pour le prénom, on permet la recherche partielle.
				//Non sensible à la casse
				if (firstName!= null && !("".equals(firstName))) {
					logger.debug("recherche par firstName ");
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("collaborater").get("firstName")),"%"+firstName.toLowerCase()+"%"));
				}
				if (deskPhoneNumber!= null && !("".equals(deskPhoneNumber))) {
					logger.debug("recherche par deskPhoneNumber ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("deskPhoneNumber"),deskPhoneNumber));
				}
				if (mobilePhoneNumber!= null && !("".equals(mobilePhoneNumber))) {
					logger.debug("recherche par mobilePhone ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("mobilePhoneNumber"),mobilePhoneNumber));
				}
				if (mailAdress!= null && !("".equals(mailAdress))) {
					logger.debug("recherche par mailAddress ");
					predicates.add(criteriaBuilder.equal(root.get("collaborater").get("mailAdress"),mailAdress));
				}
				
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}
			
		});

	}
	


	/**
	 * @param id id annuaire du CIL
	 * @return l'objet CIL correspondant
	 * @author Judicaël
	 * @version V0.1
	 */
	@Override
	public ItCorrespondant findItCorrespondantByCollaboraterId(String id) {
		
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(id);
		if (entity == null) {
			if (logger.isErrorEnabled()){
				String sLogError = "Pas de colaborateur trouvé pour id : "+id;
				logger.error(sLogError);
			}

			return null;
		}
		else {
			return entityMapper.mapToDomain(entity);
		}

	}

	/**
	 * Suppression d'un CIL en base (US008)
	 * @param itCorrespondant CIL
	 */
	@Override
	public boolean delete(ItCorrespondant itCorrespondant) {
		
		try {
			ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(itCorrespondant.getCollaboraterId());
			itCorrespondantRepository.delete(entity);
		}catch (Exception e){
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,"Pb JPA lors de la suppression d'un utilisateur");
		}
		return true;
	}
	/**
	 * Spring Security : mise à jour du mot de passe encodé
	 * @param  idAnnuaire
	 * @param  newEncryptedPassword
	 */
	@Override
	public boolean updatePassword(String idAnnuaire, String newEncryptedPassword) {

		try {
			ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
			entity.setEncryptedPassword(newEncryptedPassword);
			itCorrespondantRepository.save(entity);
		}catch (Exception e){
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,"Pb JPA lors de la mise à jour du mot de passe d'un utilisateur");
		}
		return true;
	}

	@Override
	public Long countItCorrespondant() {
		return itCorrespondantRepository.count();
	}

	@Override
	public boolean createRoleCILtoCollab(String idAnnuaire, Set<RoleTypeEnum> roles, String password) {

		CollaboraterEntity collabEntity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (collabEntity == null) {
			String msg = "Pas de colaborateur trouvé pour id : " + idAnnuaire;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND5, msg);
		} else {
			ItCorrespondantEntity entityRepo = itCorrespondantRepository.findByCollaboraterCollaboraterId(idAnnuaire);
			if (entityRepo != null) {
				String msg = "Le rôle cil existe déjà pour ce collaborateur : " + idAnnuaire;
				logger.error(msg);
				throw new AllReadyExistException(ExceptionListEnum.ALLREADY3, msg);
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

