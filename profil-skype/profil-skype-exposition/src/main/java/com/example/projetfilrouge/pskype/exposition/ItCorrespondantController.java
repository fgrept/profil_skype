package com.example.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;
import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

import com.example.projetfilrouge.pskype.application.ICollaboraterManagment;
import com.example.projetfilrouge.pskype.application.IItCorrespondantManagment;

import com.example.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;
import com.example.projetfilrouge.pskype.dto.CollaboraterDto;
import com.example.projetfilrouge.pskype.dto.ItCorrespondantDtoCreate;
import com.example.projetfilrouge.pskype.dto.ItCorrespondantDtoResult;
import com.example.projetfilrouge.pskype.dto.ItCorrespondantDtoSearch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * Classe exposant des API rest dédiées à l'itCorrespondant
 * @author La Fabrique
 * Spring security : classe correspondant au module de paramétrage, réservée aux administrateurs
 */
@RestController
@RequestMapping("/v1/user")
@Secured("ROLE_ADMIN")
@Api(value = "It correspondant REST Controller : contient toutes les opérations pour manager un It correspondant")
//@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*", exposedHeaders = {"count"})
public class ItCorrespondantController {
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantController.class);
	
	@Autowired
	private IItCorrespondantManagment userManagment;
	
	@Autowired 
	ICollaboraterManagment collaboraterManagment;
	
	/**
	 * Création d'un it correspondant en même temps qu'un collaborateur
	 * Nota : ce use cas n'existe pas actuellement et risque d'introduire des données erronées en base // REFERENTIEL
	 * car la recherche des collaborateurs dans l'IHM se fait de toute manière à partir des données en BDD et pas
	 * depuis le référentiel.
	 *  
	 * @param dto
	 * @return
	 */

	
	@PostMapping("create")
	@ApiOperation(value = "Crée un it correspondant")
	@ApiResponses(value = {
			@ApiResponse(code = 201,message = "création effectuée"),
			@ApiResponse(code = 404,message = "It correspondant non créé (collaborateur déjà existant)"),
			@ApiResponse(code = 409,message = "collaborateur ayant deja le role de cil")
	})
	public ResponseEntity<String> createItCorrespondantFromCollab(@Valid @RequestBody ItCorrespondantDtoCreate dto) {
		
		boolean isCreated = false;

		logger.info("it correspondant DTO" +dto.toString());
		//La création d'un it correspondant se fait suite à une recherche en base des collaborateurs
		//Donc on reçoit juste l'id du collaborateur et les rôles que l'on souhaite lui donner.
		//Les données sont controllés via le valideur de bean dto
		 try {
			isCreated = userManagment.createItCorrespondant(dto.getCollaboraterId(), dto.getRoles());
		} catch (NotFoundException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg, e);
		} catch (AllReadyExistException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.CONFLICT, msg, e);
		}

		if (isCreated) {
			logger.info("Création it correspondant");
			return new ResponseEntity<String>("Role CIL crée",HttpStatus.CREATED);
		}else {
			String msg = "Pb technique lors de la création du rôle cil";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
		
	}
	@GetMapping("get/{collaboraterId}")
	@ApiOperation(value = "Récupère l'it correspondant à partir de son id annuaire")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, mise à jour effectuée"),
			@ApiResponse(code = 400,message = "id annuaire incorrect en entrée"),
			@ApiResponse(code = 404,message = "it correspondant non trouvé en base")
	})
	public ResponseEntity<ItCorrespondantDtoResult> getItCorrespondantById(@PathVariable("collaboraterId") String collaboraterId) {

		if (collaboraterId.length()>17) {
			String msg = "id collaborateur : " + collaboraterId + "incorrect";
			throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, msg);
		}
		ItCorrespondant itCorrespondantResult = userManagment.findItCorrespondantById(collaboraterId);
		if (itCorrespondantResult == null) {
			String msg = "Utilsateur non trouvé pour le collaborateur "+collaboraterId;
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
		}
		return new ResponseEntity<ItCorrespondantDtoResult>(mapperDomainToDto(itCorrespondantResult), HttpStatus.OK);
	}

	/**
	 * API permettant de récupérer l'ensemble des utilisateurs présents en table
	 * @return Liste des IT Correspondant
	 */
	@GetMapping("/list")
	@ApiOperation(value = "Récupère l'ensemble des it correspondant stockés")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<ItCorrespondantDtoResult>> listItCorrespondant(){
		
		List<ItCorrespondantDtoResult> listDto = new ArrayList<ItCorrespondantDtoResult>();
		for(ItCorrespondant itCorrespondant:userManagment.listItCorrespondant()) {
			listDto.add(mapperDomainToDto(itCorrespondant));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("count",String.valueOf(listDto.size()));
		return ResponseEntity.ok().header("count", String.valueOf(listDto.size())).body(listDto);
	}

	@GetMapping("/count")
	@ApiOperation("Compte le nombre d'it Correspondant")
	@ApiResponse(code=200, message = "Nombre retourné")
	public ResponseEntity<Long> countItCorrespondant(){
		return new ResponseEntity<Long>(userManagment.countItCorrespondant(),HttpStatus.OK);
	}

	
	/**
	 * Version finale de la recherche d'un CIL (pas de recherche sur l'UO pour le moment)
	 * @param dtoSearch ItCorrespondantDtoSearch
	 * @return Liste des IT Correspondant
	 */
	@GetMapping("/list/criteria")
	@ApiOperation(value = "Récupère l'ensemble des it correspondant stockés en fonction de critères de recherche")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<ItCorrespondantDtoResult>> listItCorrespondantFilters(@RequestBody ItCorrespondantDtoSearch dtoSearch){
		
		ItCorrespondant itCorrespondant=mapperDtoToDomain(dtoSearch);
		itCorrespondant.addRole(dtoSearch.getRole());
		List<ItCorrespondantDtoResult> listDto = new ArrayList<ItCorrespondantDtoResult>();
		for(ItCorrespondant itCorrespondantResult:userManagment.listItCorrespondantFilters(itCorrespondant)) {
			listDto.add(mapperDomainToDto(itCorrespondantResult));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("count",String.valueOf(listDto.size()));
		return ResponseEntity.ok().header("count", String.valueOf(listDto.size())).body(listDto);
	}

	
	@PutMapping("uprole/{id}/{role}")
	@ApiOperation(value = "Met à jour un rôle à partir d'un id annuaire")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, mise à jour effectuée"),
			@ApiResponse(code = 404,message = "it correspondant non trouvé en base oub pb lors de la mise à jour"),
			@ApiResponse(code = 400,message = "syntaxe incorrect de la requête")
	})
	public ResponseEntity<String> updateRoleItCorrespondant(@PathVariable("id") String id, @PathVariable("role") String[] roleInput) {

		boolean isUpdate = false;

		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		for (String role : roleInput) {
			if ("ROLE_ADMIN".equals(role)){
				roles.add(RoleTypeEnum.ROLE_ADMIN);
				logger.info("ajout du role admin");
			}
			if ("ROLE_RESP".equals(role)){
				roles.add(RoleTypeEnum.ROLE_RESP);
				logger.info("ajout du role resp");
			}
			if ("ROLE_USER".equals(role)){
				roles.add(RoleTypeEnum.ROLE_USER);
				logger.info("ajout du role user");
			}
		}
		try {
			isUpdate = userManagment.updateRoleItCorrespondant(id, roles);
		} catch (NotFoundException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg, e);
		}

		if (isUpdate) {
			logger.debug("Mise à jour effectuée");
			return new ResponseEntity<String>("Mise à jour des rôles cil effectuée", HttpStatus.OK);
		}else {
			String msg = "Pb technique lors de la mise à jour du rôle cil";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
	}
	
	
	/**
	 * Mise à jour du mot de passe
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */	
	@PutMapping("/updatepassword/{id}/{oldpass}/{newpass}")
	@ApiOperation(value = "Met à jour le mot de passe d'un utilisateur")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, mise à jour effectuée"),
			@ApiResponse(code = 404,message = "Pas de mise à jour : problème lors de la mise à jour en base"),
			@ApiResponse(code = 400,message = "Pas de mise à jour : ancien et nouveau mot de passes identiques")
	})
	public ResponseEntity<String> updateRoleItCorrespondant(@PathVariable("id") String id, @PathVariable("oldpass") String oldPassword, @PathVariable("newpass") String newPassword) {
		
		boolean isUpdate = false; 
				
		if (oldPassword.equals(newPassword)) {
			String msg = "Les 2 passwords doivent être différents";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
		}
		try {
			isUpdate = userManagment.updatePasswordItCorrespondant(id, oldPassword, newPassword);
		} catch (NotFoundException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error( msg);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg, e);
		}
		
		if(isUpdate) {
			return new ResponseEntity<String>("Mise à jour password effectuée", HttpStatus.OK);
		}else {
			String msg = "Un pb est survenu lors de la mise à jour du mot de passe";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
	}

	
	@ApiOperation(value = "Supprime un utilisateur")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, suppression effectuée"),
			@ApiResponse(code = 404,message = "Utilisateur non supprimé : utilisateur absent ou problème lors de la suppression en base")
	})
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteItCorrespondant(@PathVariable("id") String id){
		
		boolean isDeleted = false;
		try {
			isDeleted = userManagment.deleteItCorrespondant(id);
		} catch (NotFoundException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg, e);
		}
		
		if (isDeleted) {
			return new ResponseEntity<String>("Suppression des droits effectués", HttpStatus.OK);
		}else {
			String msg = "Un pb est survenu lors de la suppression du cil";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
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
		return new ItCorrespondant(dto.getLastName(),dto.getFirstName(),dto.getCollaboraterId(),dto.getDeskPhoneNumber(),dto.getMobilePhoneNumber(),dto.getMailAdress(), orgaUnit);
		}
	
	
	/**
	 * Mapper de type It Correspondant vers collaborater(données de niveau collaborateur)
	 * A voir si dans un second, ce mapper n'est pas externalisé dans une classe dédiée afin de ne pas exposer les objets de niveau domaine
	 * @param itCorrespondant
	 * @return dto ItCorrespondantDtoResult
	 */
	private ItCorrespondantDtoResult mapperDomainToDto(ItCorrespondant itCorrespondant) {

		ItCorrespondantDtoResult dto = new ItCorrespondantDtoResult(itCorrespondant.getCollaboraterId(),
				itCorrespondant.getLastNamePerson(),
				itCorrespondant.getFirstNamePerson(),
				itCorrespondant.getDeskPhoneNumber(),
				itCorrespondant.getMobilePhoneNumber(),
				itCorrespondant.getMailAdress(),
				itCorrespondant.getOrgaUnit().getOrgaUnityCode(),
				itCorrespondant.getOrgaUnit().getOrgaUnityType(),
				itCorrespondant.getOrgaUnit().getOrgaShortLabel(),
				itCorrespondant.getOrgaUnit().getOrgaSite().getSiteCode(),
				itCorrespondant.getOrgaUnit().getOrgaSite().getSiteName(),
				itCorrespondant.getOrgaUnit().getOrgaSite().getSiteAddress(),
				itCorrespondant.getOrgaUnit().getOrgaSite().getSitePostalCode(),
				itCorrespondant.getOrgaUnit().getOrgaSite().getSiteCity(),
				itCorrespondant.getRoles());

		return dto;
	}
}
