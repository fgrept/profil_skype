package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
 * Création en base d'un ItCorrespondant 
 * @param ItCorrespondant
 * @return null
 * 
 */
	@Override
	public void createFull(ItCorrespondant itCorrespondant) {
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterId(itCorrespondant.getCollaboraterId());
		if (entity==null) {
			entity = entityMapper.mapToEntity(itCorrespondant);
			itCorrespondantRepository.save(entity);
		}else {
			throw new RuntimeException("It correspondant "+itCorrespondant.getCollaboraterId()+" existe déjà");
		}
	}
	
	/**
	 * US008
	 * Mise à jour des roles du CIL 
	 * Le Set de rôles annule et remplace les rôles existants
	 * @author Judicaël
	 */
	@Override
	public void update(String idAnnuaire, Set<RoleTypeEnum> roles) {
		
		System.out.println("Mise à jour du rôle It Correspondant");
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterId(idAnnuaire);
		if (entity == null) {
			throw new RuntimeException("Mise à jour impossible, id : "+idAnnuaire+" non trouvé");
		}else {
	
	//		for (RoleTypeEnum role : roles) {
	//			entity.addRoles(role);
	//}
			entity.setRoles(roles);
			itCorrespondantRepository.save(entity);
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
		for (ItCorrespondantEntity entity : itCorrespondantRepository.findByCollaboraterIdNotNull()) {
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
		
		System.out.println("findAllItCorrespondantEntityFilters :");
		System.out.println("id "+id);
		System.out.println("lastName "+ lastName);
		System.out.println("firstName "+ firstName);
		List<ItCorrespondantEntity> listItCorrespondantEntity = new ArrayList<ItCorrespondantEntity>();
		listItCorrespondantEntity = itCorrespondantRepository.findAll(new Specification<ItCorrespondantEntity>() {
			
			@Override
			public Predicate toPredicate(Root<ItCorrespondantEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (id!= null && id != "") {
					System.out.println("recherche par id "+id);
					predicates.add(criteriaBuilder.equal(root.get("collaboraterId"),id));
				}
				//Pour le nom, on permet la recherche partielle.
				//Non sensible à la casse
				if (lastName!= null && lastName != "") {
					System.out.println("recherche par lastName "+ lastName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),"%"+lastName.toLowerCase()+"%"));
				}
				//Pour le prénom, on permet la recherche partielle.
				//Non sensible à la casse
				if (firstName!= null && firstName != "") {
					System.out.println("recherche par firstName "+ firstName);
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),"%"+firstName.toLowerCase()+"%"));
				}
				if (deskPhoneNumber!= null && deskPhoneNumber != "") {
					System.out.println("recherche par deskPhoneNumber "+ deskPhoneNumber);
					predicates.add(criteriaBuilder.equal(root.get("deskPhoneNumber"),deskPhoneNumber));
				}
				if (mobilePhoneNumber!= null && mobilePhoneNumber != "") {
					System.out.println("recherche par mobilePhone "+ mobilePhoneNumber);
					predicates.add(criteriaBuilder.equal(root.get("mobilePhoneNumber"),mobilePhoneNumber));
				}
				if (mailAdress!= null && mailAdress != "") {
					System.out.println("recherche par mailAddress "+ mailAdress);
					predicates.add(criteriaBuilder.equal(root.get("mailAdress"),mailAdress));
				}
				
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}
			
		});
		for (ItCorrespondantEntity entity :listItCorrespondantEntity) {
			System.out.println("nom "+entity.getLastName());
			System.out.println("prénom "+entity.getFirstName());
			System.out.println("--------");
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
		
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterId(id);
		if (entity == null) {
			throw new RuntimeException("Pas de colaborateur trouvé pour id : "+id);
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
	public void delete(ItCorrespondant itCorrespondant) {
		
		ItCorrespondantEntity entity = entityMapper.mapToEntity(itCorrespondant);
		itCorrespondantRepository.delete(entity);
	}
	/**
	 * Spring Security : mise à jour du mot de passe encodé
	 * @param String idAnnuaire
	 * @param String newEncryptedPassword
	 */
	@Override
	public void updatePassword(String idAnnuaire, String newEncryptedPassword) {
		
		ItCorrespondantEntity entity = itCorrespondantRepository.findByCollaboraterId(idAnnuaire);
		entity.setEncryptedPassword(newEncryptedPassword);
		itCorrespondantRepository.save(entity);
	}

	@Override
	public void createRoleCILtoCollab(String idAnnuaire, Set<RoleTypeEnum> roles) {

		CollaboraterEntity collabEntity = collaboraterRepository.findByCollaboraterId(idAnnuaire);
		if (collabEntity == null) {
			throw new RuntimeException("Pas de colaborateur trouvé pour id : "+idAnnuaire);
		}
		else {
			ItCorrespondantEntity entityRepo = itCorrespondantRepository.findByCollaboraterId(idAnnuaire);
			if (entityRepo != null) {
				throw new RuntimeException("Un rôle CIL existe déjà pour ce collaborateur : " + idAnnuaire);
			} else {
				Collaborater collab = collabMapper.mapToDomain(collabEntity);
				ItCorrespondant itCorresp = new ItCorrespondant();
				itCorresp.setFirstNamePerson(collab.getFirstNamePerson());
				itCorresp.setLastNamePerson(collab.getLastNamePerson());
				itCorresp.setDeskPhoneNumber(collab.getDeskPhoneNumber());
				itCorresp.setMobilePhoneNumber(collab.getMobilePhoneNumber());
				itCorresp.setMailAdress(collab.getMailAdress());
				itCorresp.setOrgaUnit(collab.getOrgaUnit());
				itCorresp.setCollaboraterId(collab.getCollaboraterId());
				itCorresp.setRoles(roles);
				ItCorrespondantEntity itCorrespEntity = entityMapper.mapToEntity(itCorresp);			
				itCorrespondantRepository.save(itCorrespEntity);
			}
		}		
	}

}

