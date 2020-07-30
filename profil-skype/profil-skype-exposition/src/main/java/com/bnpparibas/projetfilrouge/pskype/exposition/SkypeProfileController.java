package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ICollaboraterManagment;
import com.bnpparibas.projetfilrouge.pskype.application.IItCorrespondantManagment;
import com.bnpparibas.projetfilrouge.pskype.application.ISkypeProfileManagement;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.NotFoundException;
import com.bnpparibas.projetfilrouge.pskype.dto.CollaboraterDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDtoCreate;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDtoSearch;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//import jdk.internal.net.http.common.Log;

/**
 * Classe exposant des API rest dédiées à profil Skype
 * @author La Fabrique
 *
 */
@RestController
@RequestMapping("/profile")
@Secured({"ROLE_USER","ROLE_RESP","ROLE_ADMIN"})
@Api(value = "Skype profile REST Controller : contient toutes les opérations pour manager profil skype")
public class SkypeProfileController {
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileController.class);
	
	@Autowired
	private ISkypeProfileManagement skypeProfileManagement;
	
	@Autowired
	private ICollaboraterManagment collabManagement;

	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/create")
	@ApiOperation(value = "Crée un profil skype")
	@ApiResponses(value = {
			@ApiResponse(code = 201,message = "création effectuée"),
			@ApiResponse(code = 204,message = "Données obligatoire absentes en entrée : id annuaire du collaborateur, adresse sip ou id annuaire de l'it correspondant"),
			@ApiResponse(code = 304,message = "Erreur lors de la création du profil skype")
	})
	public ResponseEntity<String> createSkypeProfil(@Valid @RequestBody SkypeProfileDtoCreate skypeProfile) {
		
		// on récupère le collaborateur dans sa totalité avant de le passer
		// au service applicatif.
		Collaborater collab = collabManagement.findCollaboraterbyIdAnnuaire(skypeProfile.getCollaboraterId());
		String idAnnuaireCIL = skypeProfile.getItCorrespondantId();
		String comment = skypeProfile.getEventComment();
		boolean isCreated = false;
		
		if (collab == null) {
			logger.error("id annuaire collaborateur non trouvé : "+ skypeProfile.getCollaboraterId());
			return new ResponseEntity<String>("id annuaire collaborateur non trouvé en base : "+ skypeProfile.getCollaboraterId(), HttpStatus.NOT_FOUND);
		}
		
		SkypeProfile profilWithCollab = mapDtoToDomain(skypeProfile, collab);
		try {
			isCreated = skypeProfileManagement.addNewSkypeProfile (profilWithCollab,idAnnuaireCIL,comment);
		} catch (AllReadyExistException e) {
			logger.error("exception déclenchée : " + e.getMessage());
			return new ResponseEntity<String>(e.getCode() + " - " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		if (isCreated) {
			return new ResponseEntity<String>("Profil skype créé", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("erreur lors de la création du profil", HttpStatus.NOT_FOUND);
		}

	}	

	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/delete/{sip}")
	@ApiOperation(value = "Supprime un profil skype")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, suppression effectuée"),
			@ApiResponse(code = 404,message = "Profil skype non supprimé : absent ou problème lors de la suppression en base")
	})
	public ResponseEntity<String> deleteSkypeProfil(@PathVariable("sip") String sip) {
		
		boolean isDeleted = false;
		
		logger.debug("sip en entrée : " + sip);
		try {
			isDeleted = skypeProfileManagement.deleteSkypeProfile(sip);	
		} catch (NotFoundException e) {
			logger.error("exception déclenchée : " + e.getMessage());
			return new ResponseEntity<String>(e.getCode() + " - " + e.getMessage(), HttpStatus.NOT_FOUND);
		}


		if (isDeleted) {
			logger.info("Exposition : Suppression effectuée");
			return new ResponseEntity<String>("Supression effectuée", HttpStatus.OK);

		} else {
			logger.error("Pb lors de la supression");
			return new ResponseEntity<String>("Pb lors de la supression", HttpStatus.NOT_FOUND);
		}
	}

	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/update")
	@ApiOperation(value = "Met à jour les données d'un profil skype")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, mise à jour effectuée"),
			@ApiResponse(code = 204,message = "Données obligatoire absentes en entrée ou collaborateur absent de la base"),
			@ApiResponse(code = 304,message = "Problème de la mise à jour")
	})
	public ResponseEntity<String> updateSkypeProfil(@Valid @RequestBody SkypeProfileDtoCreate skypeProfile) {
	
		boolean isModified = false;
				
		// on récupère le collaborateur dans sa totalité avant de le passer
		// au service applicatif.
		Collaborater collab = collabManagement.findCollaboraterbyIdAnnuaire(skypeProfile.getCollaboraterId());
		String idAnnuaireCIL = skypeProfile.getItCorrespondantId();
		String comment = skypeProfile.getEventComment();
		
		if (collab == null) {
			return new ResponseEntity<String>("Collaborateur inexistant en base pour l'idAnnuaire : "
						+ skypeProfile.getCollaboraterId(), HttpStatus.NOT_FOUND);
		} else {
			SkypeProfile profilToChange = mapDtoToDomain(skypeProfile, collab);
			
			try {
				isModified = skypeProfileManagement.updateSkypeProfile(profilToChange, idAnnuaireCIL,comment);

			} catch (NotFoundException e) {
				logger.error("exception déclenchée : " + e.getMessage());
				return new ResponseEntity<String>(e.getCode() + " - " + e.getMessage(), HttpStatus.NOT_FOUND);
			}
			
			if (isModified) {
				return new ResponseEntity<String>("Profil mis à jour", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Pb lors de la mise à jour du profil", HttpStatus.NOT_FOUND);
			}
		}

	}
	
//	@Secured({"ROLE_RESP","ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/list/all")
	@ApiOperation(value = "Récupère l'ensemble des profils skype stockés")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfil(){
		
		List<SkypeProfile> profilListDom = skypeProfileManagement.findAllSkypeProfile();
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}
	
	
	@GetMapping("/list/all/{numberPage}/{sizePage}/{criteria}")
	@ApiOperation(value = "Récupère un ensemble de profils skype stockés selon des critères de pagination")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, liste retournée"),
			@ApiResponse(code = 304,message = "Critères de pagination incorrects"),
	})
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfilPage(@PathVariable("numberPage") int numberPage, @PathVariable("sizePage") int sizePage, @PathVariable("criteria") String criteria){
		
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		if (numberPage<0) {
			logger.error("numéro de page négatif");
			return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto,HttpStatus.NOT_MODIFIED);
		}
		if (sizePage<=0) {
			logger.error("taille de la page insuffisante");
			return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto,HttpStatus.NOT_MODIFIED);
		}
		if (criteria == null) {
			criteria="";
		}
		List<SkypeProfile> profilListDom = skypeProfileManagement.findAllSkypeProfilePage(numberPage,sizePage,criteria,true);
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Compte l'ensemble des profils skype stockés")
	@ApiResponse(code = 200,message ="Ok, nombre de profils skype retourné")
	@GetMapping("/count")
	public ResponseEntity<Long> countSkypeProfile(){
		return new ResponseEntity<Long>(skypeProfileManagement.countSkypeProfile(), HttpStatus.OK);
	}	


	@GetMapping("/list/criteria")
	@ApiOperation(value = "Récupère l'ensemble des profils skype stockés en fonction de critères de recherche")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfilByCriteria(@RequestBody SkypeProfileDtoSearch searchCriteria){
		
		SkypeProfile profilDom = mapDtoSearchToDomain(searchCriteria);
		
		List<SkypeProfile> profilListDom = skypeProfileManagement.findSkypeProfileWithCriteria(profilDom);
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}

	
	@GetMapping("/list/criteria/{numberPage}/{sizePage}/{criteria}")
	@ApiOperation(value = "Récupère un ensemble de profils skype stockés selon des critères de pagination et des critères de recherches")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Ok, liste retournée"),
			@ApiResponse(code = 304,message = "Critères de pagination incorrects"),
	})
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfilByCriteriaPage(@RequestBody SkypeProfileDtoSearch searchCriteria,@PathVariable("numberPage") int numberPage, @PathVariable("sizePage") int sizePage, @PathVariable("criteria") String criteria){
		
		logger.debug("numberPage : "+numberPage+" sizePage : "+sizePage+" criteria : "+criteria);
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		if (numberPage<0) {
			logger.error("numéro de page négatif");
			return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto,HttpStatus.NOT_MODIFIED);
		}
		if (sizePage<=0) {
			logger.error("taille de la page insuffisante");
			return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto,HttpStatus.NOT_MODIFIED);
		}
		if (criteria == null) {
			criteria="";
		}
		SkypeProfile profilDom = mapDtoSearchToDomain(searchCriteria);
		
		List<SkypeProfile> profilListDom = skypeProfileManagement.findSkypeProfileWithCriteriaPage(profilDom,numberPage,sizePage,criteria,true);
		
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}
	
	
	private SkypeProfile mapDtoToDomain (SkypeProfileDtoCreate profilDto, Collaborater collab) {
		
		// la date d'expiration est calculée par le domain
		SkypeProfile profilDom = new SkypeProfile(profilDto.getSIP(), Boolean.valueOf(profilDto.getEnterpriseVoiceEnabled()),
				profilDto.getVoicePolicy(), profilDto.getDialPlan(), profilDto.getSamAccountName(),
				Boolean.valueOf(profilDto.getExUmEnabled()),profilDto.getExchUser(),profilDto.getObjectClass(),
				collab);
		
		profilDom.setStatusProfile(profilDto.getStatusProfile());
		logger.info(profilDom.toString());
		return profilDom;
		
	}

	private SkypeProfile mapDtoSearchToDomain (SkypeProfileDtoSearch profilDto) {
		
		// création d'un "collaborateur" fictif pour les critères de recherches
		Collaborater collab = new Collaborater();
		OrganizationUnity uo = new OrganizationUnity();
		uo.setOrgaUnityCode(profilDto.getOrgaUnityCode());
		Site site = new Site();
		site.setSiteCode(profilDto.getSiteCode());
		uo.setOrgaSite(site);
		collab.setOrgaUnit(uo);
		
		// suite du mapper pour les attributs de recherche du profil
		SkypeProfile profilDom = new SkypeProfile(profilDto.getSIP(), Boolean.valueOf(profilDto.getEnterpriseVoiceEnabled()),
				profilDto.getVoicePolicy(), profilDto.getDialPlan(), profilDto.getSamAccountName(),
				Boolean.valueOf(profilDto.getExUmEnabled()),profilDto.getExchUser(),profilDto.getObjectClass(),
				collab);
		
		return profilDom;
		
	}

	
	private SkypeProfileDtoSearch mapDomainToDtoSearch (SkypeProfile profil) {
		
		String siteCode = "";
		String orgaUnitCode = "";
		String collaboraterId = "";
		String lastName = "";
		String firstName = 	"";
		
		if (profil.getCollaborater()!=null) {
			collaboraterId = profil.getCollaborater().getCollaboraterId();
			lastName = profil.getCollaborater().getLastNamePerson();
			firstName = profil.getCollaborater().getFirstNamePerson();
			if (profil.getCollaborater().getOrgaUnit() != null) {
				orgaUnitCode = profil.getCollaborater().getOrgaUnit().getOrgaUnityCode();
				if (profil.getCollaborater().getOrgaUnit().getOrgaSite() != null) {
					siteCode = profil.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode();
				}
			}	
		}
		
		SkypeProfileDtoSearch profilDto = new SkypeProfileDtoSearch(profil.getSIP(), Boolean.toString(profil.isEnterpriseVoiceEnabled()), profil.getVoicePolicy(),
				profil.getDialPlan(), profil.getSamAccountName(), Boolean.toString(profil.isExUmEnabled()), profil.getExchUser(), profil.getObjectClass(),
				profil.getStatusProfile(), profil.getExpirationDate(),
				collaboraterId, firstName, lastName,
				orgaUnitCode, siteCode);
		
		return profilDto;
		
	}
}