package com.example.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;
import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.projetfilrouge.pskype.application.ICollaboraterManagment;

import com.example.projetfilrouge.pskype.dto.CollaboraterDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe exposant des API rest dédiées au Collaborater
 * @author La Fabrique
 * Spring security : classe correspondant au module de paramétrage, réservée aux administrateurs
 */
@RestController
@RequestMapping("/v1/collaborater")
@Secured({"ROLE_RESP","ROLE_ADMIN"})
@Api(value = "Collaborater REST Controller : contient toutes les opérations pour manager un collaborateur")
//@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*", exposedHeaders = {"count"})
public class CollaboraterController {
	
	private static Logger logger = LoggerFactory.getLogger(CollaboraterController.class);
	
	@Autowired
	private ICollaboraterManagment collaboraterManagment;
	
	@GetMapping("/list")
	@ApiOperation(value = "Récupère l'ensemble des collaborateurs stockés")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<CollaboraterDto>> listCollaborater(){
		
		List<Collaborater> list =  collaboraterManagment.listCollaborater();
		List<CollaboraterDto> dto = new ArrayList<CollaboraterDto>();
		for (Collaborater collaborater:list) {
			dto.add(mapperDomainToDto(collaborater));
		}
		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}

	@GetMapping("/count")
	@ApiOperation("Compte le nombre de collaborateurs")
	@ApiResponse(code=200, message = "Nombre de collaborateurs retourné")
	public ResponseEntity<Long> countCollaborater(){
		return new ResponseEntity<Long>(collaboraterManagment.countCollaborater(),HttpStatus.OK);
	}
	
	@GetMapping("/list/{numberPage}/{sizePage}/{criteria}")
	@ApiOperation(value = "Récupère un ensemble de collaborateurs stockés selon des critères de pagination")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, liste retournée"),
			@ApiResponse(code = 304,message = "Critères de pagination incorrects"),
	})
	public ResponseEntity<List<CollaboraterDto>> listCollaboraterPage(@PathVariable("numberPage") int numberPage, @PathVariable("sizePage") int sizePage, @PathVariable("criteria") String criteria){
		List<CollaboraterDto> dto = new ArrayList<CollaboraterDto>();
		if (numberPage<0) {
			logger.error("numéro de page négatif");
			return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.NOT_MODIFIED);
		}
		if (sizePage<=0) {
			logger.error("taille de la page insuffisante");
			return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.NOT_MODIFIED);
		}
		if (criteria == null) {
			criteria="";
		}
		List<Collaborater> list =  collaboraterManagment.listCollaboraterSortByPage(numberPage, sizePage, criteria, true);
		
		for (Collaborater collaborater:list) {
			dto.add(mapperDomainToDto(collaborater));
		}
		return ResponseEntity.ok().header("count", String.valueOf(dto.size())).body(dto);
//		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}

	@GetMapping("/get/{collaboraterId}")
	@ApiOperation(value = "récupère le profil skype du collaborateur")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, suppression effectuée"),
			@ApiResponse(code = 304,message = "Collaborateur Id incorrect"),
			@ApiResponse(code = 204,message = "Profil skype non trouvé pour le collaborateur en entrée")
	})
	public ResponseEntity<CollaboraterDto> getByCollaboraterId(@PathVariable("collaboraterId") String collaboraterId) {

		if (collaboraterId.length()>17) {
			String msg = "id collaborateur : " + collaboraterId + "incorrect";
			throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, msg);
		}
		Collaborater collaborater = collaboraterManagment.findCollaboraterbyIdAnnuaire(collaboraterId);
		if (collaborater == null) {
			String msg = "Collaborateur non trouvé pour le collaborateur "+collaboraterId;
			logger.info(msg);
			return new ResponseEntity<CollaboraterDto>(new CollaboraterDto(), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<CollaboraterDto>(mapperDomainToDto(collaborater), HttpStatus.OK);
	}
	
	
	@PostMapping("/list/criteria/{numberPage}/{sizePage}/{criteria}")
	@ApiOperation(value = "Récupère un ensemble de collaborateurs stockés selon des critères de pagination et des critères de recherches")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, liste retournée"),
			@ApiResponse(code = 304,message = "Critères de pagination incorrects"),
	})
	public ResponseEntity<List<CollaboraterDto>> listCollaboraterCriteriaPage(@RequestBody CollaboraterDto collaboraterDto,@PathVariable("numberPage") int numberPage, @PathVariable("sizePage") int sizePage, @PathVariable("criteria") String criteria){
		
		List<CollaboraterDto> dto = new ArrayList<CollaboraterDto>();
		if (numberPage<0) {
			logger.error("numéro de page négatif");
			return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.NOT_MODIFIED);
		}
		if (sizePage<=0) {
			logger.error("taille de la page insuffisante");
			return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.NOT_MODIFIED);
		}
		if (criteria == null) {
			criteria="";
		}
		
		List<Collaborater> list =  collaboraterManagment.listCollaboraterCriteriaSortByPage(mapperDtoToDomain(collaboraterDto),numberPage, sizePage, criteria, true);
		for (Collaborater collaborater:list) {
			dto.add(mapperDomainToDto(collaborater));
		}
		return ResponseEntity.ok().header("count", String.valueOf(dto.size())).body(dto);
//		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}

	
	
	@PostMapping("/create")
	@ApiOperation(value = "Crée un collaborateur")
	@ApiResponses(value = {
			@ApiResponse(code = 201,message = "création effectuée"),
			@ApiResponse(code = 400,message = "données en entrée incorrectes"),
			@ApiResponse(code = 409,message = "Collaborateur déjà existant")
	})
	public ResponseEntity<Boolean> createCollaborater(@Valid  @RequestBody CollaboraterDto dto) {
		
		Collaborater collaborater = collaboraterManagment.findCollaboraterbyIdAnnuaire(dto.getCollaboraterId());
		if (collaborater !=null) {
			String msg = "Collaborateur déjà existant";
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.CONFLICT, msg);
		}
		boolean isCreated = collaboraterManagment.createCollaborater(mapperDtoToDomain(dto));
		return new ResponseEntity<Boolean>(isCreated,HttpStatus.CREATED);
	}
	
	
	/**
	 * Mapper de type collaborater vers It Correspondant (données de niveau collaborateur)
	 * A voir si dans un second, ce mapper n'est pas externalisé dans une classe dédiée afin de ne pas exposer les objets de niveau domaine
	 * @param dto Collaborater
	 * @return It Correspondant
	 */
	private Collaborater mapperDtoToDomain(CollaboraterDto dto) {
		
		Site site = new Site(dto.getSiteCode(),dto.getSiteName(),dto.getSiteAddress(), dto.getSitePostalCode(),dto.getSiteCity());
		OrganizationUnity orgaUnit = new OrganizationUnity(dto.getOrgaUnitCode(),dto.getOrgaUnityType(),dto.getOrgaShortLabel(),site);
		return new Collaborater(dto.getLastName(),dto.getFirstName(),dto.getCollaboraterId(),dto.getDeskPhoneNumber(),dto.getMobilePhoneNumber(),dto.getMailAdress(), orgaUnit);

		}
	
	
	/**
	 * Mapper de type It Correspondant vers collaborater(données de niveau collaborateur)
	 * A voir si dans un second, ce mapper n'est pas externalisé dans une classe dédiée afin de ne pas exposer les objets de niveau domaine
	 * @param collaborater
	 * @return dto ItCorrespondantDtoResult
	 */
	private CollaboraterDto mapperDomainToDto(Collaborater collaborater) {
		
		CollaboraterDto dto = new CollaboraterDto(collaborater.getCollaboraterId(),
				collaborater.getLastNamePerson(),
				collaborater.getFirstNamePerson(),
				collaborater.getDeskPhoneNumber(),
				collaborater.getMobilePhoneNumber(),
				collaborater.getMailAdress(),
				collaborater.getOrgaUnit().getOrgaUnityCode(),
				collaborater.getOrgaUnit().getOrgaUnityType(),
				collaborater.getOrgaUnit().getOrgaShortLabel(),
				collaborater.getOrgaUnit().getOrgaSite().getSiteCode(),
				collaborater.getOrgaUnit().getOrgaSite().getSiteName(),
				collaborater.getOrgaUnit().getOrgaSite().getSiteAddress(),
				collaborater.getOrgaUnit().getOrgaSite().getSitePostalCode(),
				collaborater.getOrgaUnit().getOrgaSite().getSiteCity());

		return dto;
	}
}
