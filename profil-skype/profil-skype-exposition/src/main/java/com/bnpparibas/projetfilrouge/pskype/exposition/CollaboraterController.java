package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ICollaboraterManagment;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.dto.CollaboraterDto;
import com.bnpparibas.projetfilrouge.pskype.dto.ItCorrespondantDtoCreate;
import com.bnpparibas.projetfilrouge.pskype.dto.ItCorrespondantDtoResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author Judicaël
 *
 */
@RestController
@RequestMapping("/collaborater")
@Secured({"ROLE_RESP","ROLE_ADMIN"})
@Api(value = "Collaborater REST Controller : contient toutes les opérations pour manager un collaborateur")
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
		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}
	
	
	@GetMapping("/list/criteria/{numberPage}/{sizePage}/{criteria}")
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
		
		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}
	
	
	@PostMapping("/create")
	@ApiOperation(value = "Crée un collaborateur")
	@ApiResponses(value = {
			@ApiResponse(code = 201,message = "création effectuée"),
			@ApiResponse(code = 204,message = "id collaborateur absent en entrée"),
			@ApiResponse(code = 304,message = "Collaborateur déjà existant")
	})
	public ResponseEntity<Boolean> createCollaborater(@RequestBody CollaboraterDto dto) {
		
		if ((dto.getCollaboraterId() == null)|| (dto.getCollaboraterId()=="")) {
			return new ResponseEntity<Boolean>(false,HttpStatus.NO_CONTENT);
		}
		Collaborater collaborater = collaboraterManagment.findCollaboraterbyIdAnnuaire(dto.getCollaboraterId());
		if (collaborater !=null) {
			return new ResponseEntity<Boolean>(false,HttpStatus.NOT_MODIFIED); 
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
		Collaborater collaborater = new Collaborater(dto.getLastName(),dto.getFirstName(),dto.getCollaboraterId(),dto.getDeskPhoneNumber(),dto.getMobilePhoneNumber(),dto.getMailAdress(), orgaUnit);
		return collaborater;
		}
	
	
	/**
	 * Mapper de type It Correspondant vers collaborater(données de niveau collaborateur)
	 * A voir si dans un second, ce mapper n'est pas externalisé dans une classe dédiée afin de ne pas exposer les objets de niveau domaine
	 * @param itCorrespondant
	 * @return dto ItCorrespondantDtoResult
	 */
	private CollaboraterDto mapperDomainToDto(Collaborater collaborater) {
		
		CollaboraterDto dto = new CollaboraterDto();
		dto.setCollaboraterId(collaborater.getCollaboraterId());
		dto.setDeskPhoneNumber(collaborater.getDeskPhoneNumber());
		dto.setFirstName(collaborater.getFirstNamePerson());
		dto.setLastName(collaborater.getLastNamePerson());
		dto.setMailAdress(collaborater.getMailAdress());
		dto.setMobilePhoneNumber(collaborater.getMobilePhoneNumber());
		if (collaborater.getOrgaUnit() !=null) {
			dto.setOrgaShortLabel(collaborater.getOrgaUnit().getOrgaShortLabel());
			dto.setOrgaUnitCode(collaborater.getOrgaUnit().getOrgaUnityCode());
			dto.setOrgaUnityType(collaborater.getOrgaUnit().getOrgaUnityType());
			if (collaborater.getOrgaUnit().getOrgaSite()!=null) {
				dto.setSiteAddress(collaborater.getOrgaUnit().getOrgaSite().getSiteAddress());
				dto.setSiteCity(collaborater.getOrgaUnit().getOrgaSite().getSiteCity());
				dto.setSiteCode(collaborater.getOrgaUnit().getOrgaSite().getSiteCode());
				dto.setSiteName(collaborater.getOrgaUnit().getOrgaSite().getSiteName());
				dto.setSitePostalCode(collaborater.getOrgaUnit().getOrgaSite().getSitePostalCode());
			}
		}
		return dto;
	}
}
