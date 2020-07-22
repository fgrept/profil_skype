package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

import com.bnpparibas.projetfilrouge.pskype.application.IItCorrespondantManagment;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.dto.CollaboraterDto;
import com.bnpparibas.projetfilrouge.pskype.dto.ItCorrespondantDtoCreate;
import com.bnpparibas.projetfilrouge.pskype.dto.ItCorrespondantDtoSearch;
/**
 * Classe exposant des API rest dédiées à l'itCorrespondant
 * @author Judicaël
 * Spring security : classe correspondant au module de paramétrage, réservée aux administrateurs
 */
@RestController
@RequestMapping("/users")
@Secured("ROLE_ADMIN")
public class ItCorrespondantController {
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantController.class);
	
	@Autowired
	private IItCorrespondantManagment correspondantManagement;
	
	/**
	 * Création d'un it correspondant
	 * @param dto
	 * @return
	 */
	@PostMapping("create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Boolean> createItCorrespondant(@RequestBody ItCorrespondantDtoCreate dto) {
		
		//La création d'un utilisateur requiert toutes les données nécessaires à la création d'un it correspondant donc aussi
		//collaborateur, personn, uo et site s'ils n'existent pas déjà
		ItCorrespondant itCorrespondant=mapperDtoToDomain(dto);
		itCorrespondant.setPassword(dto.getPassword());
		boolean isCreated = correspondantManagement.createItCorrespondant(itCorrespondant);
		if (isCreated) {
			logger.info("Création it correspondant");
			return new ResponseEntity<Boolean>(true,HttpStatus.CREATED);
		}else {
			return new ResponseEntity<Boolean>(false,HttpStatus.NOT_MODIFIED); 
		}
	}
	
	/**
	 * API dédiée aux tests pour ajouter manuellement un ensemble d'it correspondant
	 * @return
	 */
	@GetMapping("createauto")
	public String CreateUserAuto() {
		
//		ItCorrespondant itCorrespondant = new ItCorrespondant("Grept", "Fabien", "116453", "0140401234", "0640140102", "fabien.toto@gmail.com");
//		correspondantManagement.createCIL("Grept", "Fabien", "000004", "0140401234", "0640140102", "fabien.toto@gmail.com");
//		correspondantManagement.createCIL("ElOuarak", "Mehdi", "000005", "0140402345", "0640140304", "mehdi.titi@gmail.com");
//		correspondantManagement.createCIL("Tige", "Judicael", "000006", "0140403456", "0640140405", "judi.tata@gmail.com");	
//		correspondantManagement.createItCorrespondant"Tige", "Judi", "000020", "0140403456", "0640140405", "judi.tatatoto@gmail.com","000000");
		//Password associé : $2a$10$mLw0BcfmsIszyoBvIiqTWOcaBW8vc8.VoOZ2u27xroUi638aeUvLO
//		correspondantManagement.createItCorrespondant("Tige", "Juju", "000021", "0140403456", "0640140405", "judi.tatatiti@gmail.com","000000");
		//Password associé : $2a$10$zjrjQg24MpCtPxu.ekG4yuv3eJ1tbm9PRPiD7S85w.SEGNJqx1fRy
//		correspondantManagement.createItCorrespondant("Tige", "Toto", "000022", "0140403456", "0640140405", "judi.tatatutu@gmail.com","000000");
		//Password associé : $2a$10$3E4JuVZylMiC7uZ9AJSM8uGqYgueyQKrJHjdHYNSw3FrHG.G379y6
		logger.info("Création automatique effectuée");
		return "creation effectuée";
	}
	
	/**
	 * API permettant de récupérer l'ensemble des utilisateurs présents en table
	 * @return Liste des IT Correspondant
	 */
	@GetMapping("/list")
	public ResponseEntity<List<ItCorrespondant>> listItCorrespondant(){
		
		return new ResponseEntity<List<ItCorrespondant>>(correspondantManagement.listItCorrespondant(),HttpStatus.OK);
	}

	
	/**
	 * Version finale de la recherche d'un CIL (pas de recherche sur l'UO pour le moment)
	 * @param itCorrespondant
	 * @return Liste des IT Correspondant
	 */

	@GetMapping("/search")
	public ResponseEntity<List<ItCorrespondant>> listItCorrespondantFilters(@RequestBody ItCorrespondantDtoSearch dto){
		
		ItCorrespondant itCorrespondant=mapperDtoToDomain(dto);
		itCorrespondant.addRole(dto.getRole());
		return new ResponseEntity<List<ItCorrespondant>>(correspondantManagement.listItCorrespondantFilters(itCorrespondant),HttpStatus.OK);
		
	}

	@PutMapping("uprole/{id}/{role}")
	public ResponseEntity<Boolean> updateRoleItCorrespondant(@PathVariable("id") String id, @PathVariable("role") String role) {
		
		logger.debug("id en entree " + id);
		logger.debug("role en entree " + role);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		switch (role)
		{
			case "admin":
				logger.debug("Passage à role ADMIN");
				roles.add(RoleTypeEnum.ROLE_ADMIN);
				roles.add(RoleTypeEnum.ROLE_RESP);
				roles.add(RoleTypeEnum.ROLE_USER);
				break;
			case "resp":
				logger.debug("Passage à role RESP");
				roles.add(RoleTypeEnum.ROLE_RESP);
				roles.add(RoleTypeEnum.ROLE_USER);
				break;
			case "user":
				logger.debug("Passage à role USER");
				roles.add(RoleTypeEnum.ROLE_USER);
				break;
			default:
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		boolean isUpdate = correspondantManagement.updateRoleItCorrespondant(id, roles);
		if (isUpdate) {
			logger.debug("Mise à jour effectuée");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		}
	}
	/**
	 * Mise à jour du mot de passe
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	
	@PutMapping("/updatepassword/{oldpass}/{newpass}")
	public ResponseEntity<Boolean> updateRoleItCorrespondant(@PathVariable("id") String id, @PathVariable("oldpass") String oldPassword, @PathVariable("oldpass") String newPassword) {
		
		if (oldPassword ==newPassword) {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		}
		boolean isUpdate = correspondantManagement.updatePasswordItCorrespondant(id, oldPassword, newPassword);
		if(isUpdate) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> deleteItCorrespondant(@PathVariable("id") String id){
		
		boolean isDeleted = correspondantManagement.deleteItCorrespondant(id);
		if (isDeleted) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND); 
		}
		
	}
	
	/**
	 * Mapper de type collaborater vers It Correspondant (données de niveau collaborateur)
	 * A voir si dans un second, ce mapper n'est pas externalisé dans une classe dédiée afin de ne pas exposer les objets de niveau domaine
	 * @param dto Collaborater
	 * @return It Correspondant
	 */
	private ItCorrespondant mapperDtoToDomain(CollaboraterDto dto) {
		
		Site site = new Site(dto.getSiteCode(),dto.getSiteName(),dto.getSiteAddress(), dto.getSitePostalCode(),dto.getSiteCity());
		OrganizationUnity orgaUnit = new OrganizationUnity(dto.getOrgaUnitCode(),dto.getOrgaUnityType(),dto.getOrgaShortLabel(),site);
		ItCorrespondant itCorrespondant = new ItCorrespondant(dto.getLastName(),dto.getFirstName(),dto.getCollaboraterId(),dto.getDeskPhoneNumber(),dto.getMobilePhoneNumber(),dto.getMailAdress(), orgaUnit);
		return itCorrespondant;
		}
	}
