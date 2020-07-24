package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;

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
import com.bnpparibas.projetfilrouge.pskype.application.ISkypeProfileManagement;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDtoSearch;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Classe exposant des API rest dédiées à profil Skype
 * @author La Fabrique
 *
 */
@RestController
@RequestMapping("/profile")
//@Secured({"ROLE_USER","ROLE_RESP","ROLE_ADMIN"})
public class SkypeProfileControllerOld {

	@Autowired
	private ISkypeProfileManagement skypeProfileManagement;
	
	@Autowired
	private ICollaboraterManagment collabManagement;
	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/create")
	public void createSkypeProfil(@RequestBody SkypeProfile skypeProfile, String itcorrespondantId) {
		
		skypeProfileManagement.addNewSkypeProfile(skypeProfile,itcorrespondantId); 
		
	}
	
	
	@GetMapping("/createauto")
 	public ResponseEntity<Boolean> createSkypeProfil(@RequestBody SkypeProfileDto skypeProfile) {
		
		// on récupère le collaborateur dans sa totalité avant de le passer
		// au service applicatif.
		Collaborater collab = collabManagement.findCollaboraterbyIdAnnuaire(skypeProfile.getCollaboraterId());
		String idAnnuaireCIL = skypeProfile.getItCorrespondantId();
		
		if (collab == null || idAnnuaireCIL == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		} else {
			SkypeProfile profilWithCollab = mapDtoToDomain(skypeProfile, collab);
			boolean isCreated = skypeProfileManagement.addNewSkypeProfile (profilWithCollab,idAnnuaireCIL);
			
			if (isCreated) {
				return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
			}
		}

	}
	
/*	@GetMapping("/createauto")
>>>>>>> 364ac278cb519b1f88d7a1171625d0f481988584:profil-skype/profil-skype-exposition/src/main/java/com/bnpparibas/projetfilrouge/pskype/exposition/SkypeProfileController.java
	public void createSkypeProfilAuto() {
	//  SkypeProfileDto skypeProfile =new SkypeProfileDto("juju.titi@live.toto.com", "000015","000015");
    	
		//SkypeProfile skypeProfile =new SkypeProfile("mido.82@live.com", "000017","000016");
		//skypeProfileManagement.addNewSkypeProfile(skypeProfile);
		
		System.out.println("Exposition : création effectuée");
	
	}*/
	
/*	@GetMapping("/createautowithevent")
	public void createSkypeProfilAutoWithEvent() {
		
			
		SkypeProfileDto skypeProfile =new SkypeProfileDto("mido.82@live.com", "000017","000016");
		
		SkypeProfileEventDto skypeProfileEvent =new SkypeProfileEventDto (skypeProfile, TypeEventEnum.CREATION,"CREATION DU PROFIL SKYPE","000016");
						
	
		//skypeProfileManagement.addNewSkypeProfileWithEvent(skypeProfile,skypeProfileEvent);
		
		System.out.println("Exposition : création effectuée avec Event");
		
	
	}*/
	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/delete")

		
	public ResponseEntity<Boolean> deleteSkypeProfil(@RequestBody String sip) {
		
		System.out.println(sip ) ;
		
		boolean isDeleted = skypeProfileManagement.deleteSkypeProfile(sip);	

		if (isDeleted) {
			System.out.println("Exposition : Suppression effectuée");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);

		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		}
	}

	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/update")
	public ResponseEntity<Boolean> updateSkypeProfil(@RequestBody SkypeProfileDto skypeProfile) {
		
		Collaborater collab = collabManagement.findCollaboraterbyIdAnnuaire(skypeProfile.getCollaboraterId());
		String idAnnuaireCIL = skypeProfile.getItCorrespondantId();
		
		if (collab == null || idAnnuaireCIL == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
		} else {
			SkypeProfile profilToChange = mapDtoToDomain(skypeProfile, collab);
			
			boolean isModified = skypeProfileManagement.updateSkypeProfile(profilToChange, idAnnuaireCIL);
			
			if (isModified) {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_MODIFIED);
			}
		}

	}
	
	@Secured({"ROLE_RESP","ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/list/all")
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfil(){
		
		List<SkypeProfile> profilListDom = skypeProfileManagement.findAllSkypeProfile();
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}

	@Secured({"ROLE_RESP","ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/list/criteria")
	public ResponseEntity<List<SkypeProfileDtoSearch>> listAllProfilByCriteria(@RequestBody SkypeProfileDtoSearch searchCriteria){
		
		SkypeProfile profilDom = mapDtoSearchToDomain(searchCriteria);
		
		List<SkypeProfile> profilListDom = skypeProfileManagement.findSkypeProfileWithCriteria(profilDom);
		List<SkypeProfileDtoSearch> profilListDto = new ArrayList<SkypeProfileDtoSearch>();
		
		for (SkypeProfile skypeProfile : profilListDom) {
			profilListDto.add(mapDomainToDtoSearch(skypeProfile));
		}		
		
		return new ResponseEntity<List<SkypeProfileDtoSearch>>(profilListDto, HttpStatus.OK);
	}
	
	private SkypeProfile mapDtoToDomain (SkypeProfileDto profilDto, Collaborater collab) {
		
		// la date d'expiration est calculée par le domain
		SkypeProfile profilDom = new SkypeProfile(profilDto.getSIP(), profilDto.isEnterpriseVoiceEnabled(),
				profilDto.getVoicePolicy(), profilDto.getDialPlan(), profilDto.getSamAccountName(),
				profilDto.isExUmEnabled(),profilDto.getExchUser(),profilDto.getObjectClass(),
				collab);
		
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
		SkypeProfile profilDom = new SkypeProfile(profilDto.getSIP(), profilDto.isEnterpriseVoiceEnabled(),
				profilDto.getVoicePolicy(), profilDto.getDialPlan(), profilDto.getSamAccountName(),
				profilDto.isExUmEnabled(),profilDto.getExchUser(),profilDto.getObjectClass(),
				collab);
		
		return profilDom;
		
	}
	
	private SkypeProfileDtoSearch mapDomainToDtoSearch (SkypeProfile profil) {
		
		SkypeProfileDtoSearch profilDto = new SkypeProfileDtoSearch(profil.getSIP(), profil.isEnterpriseVoiceEnabled(), profil.getVoicePolicy(),
				profil.getDialPlan(), profil.getSamAccountName(), profil.isExUmEnabled(), profil.getExchUser(), profil.getObjectClass(),
				profil.getStatusProfile(), profil.getExpirationDate(),
				profil.getCollaborater().getCollaboraterId(), profil.getCollaborater().getFirstNamePerson(), profil.getCollaborater().getLastNamePerson(),
				profil.getCollaborater().getOrgaUnit().getOrgaUnityCode(),
				profil.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode());
		
		return profilDto;
		
	}
	
	
	@PostMapping("/update")
	public void updateSkypeProfil(@RequestBody SkypeProfile skypeProfile,String itcorrespondantId) {
		
		skypeProfile.setSIP("mido.82@live.com");
		skypeProfile.setDialPlan("DP-MA");
		skypeProfile.setExchUser("mehdi.el@gmail.com");
		itcorrespondantId="000016" ;
		
		skypeProfileManagement.updateSkypeProfile(skypeProfile,itcorrespondantId);	
		
		System.out.println("Exposition : Mise à jour effectuée");
	
	}
	
//	@PostMapping("/updatewithevent")
//	public void updateSkypeProfilWithEvent(@RequestBody String sip,SkypeProfile skypeProfile,String itcorrespondantId) {
//		
//			
//	//Set de valeur pourle test
//		
//		skypeProfile.setSIP("mido.82@live.com");
//		skypeProfile.setDialPlan("DP-FR");
//		itcorrespondantId="000016" ;
//
//		
//		skypeProfileManagement.updateSkypeProfile(sip,skypeProfile,itcorrespondantId);	
//		
//		System.out.println("Exposition : Mise à jour effectuée");
//
//	
//	}
}
