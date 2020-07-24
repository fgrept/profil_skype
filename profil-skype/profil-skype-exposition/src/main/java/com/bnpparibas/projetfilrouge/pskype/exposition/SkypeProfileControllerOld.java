package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ISkypeProfileManagement;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Classe exposant des API rest dédiées à profil Skype
 * @author Judicaël
 *
 */
@RestController
@RequestMapping("/profile")
//@Secured({"ROLE_USER","ROLE_RESP","ROLE_ADMIN"})
public class SkypeProfileControllerOld {

	@Autowired
	private ISkypeProfileManagement skypeProfileManagement;
	
	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/create")
	public void createSkypeProfil(@RequestBody SkypeProfile skypeProfile, String itcorrespondantId) {
		
		skypeProfileManagement.addNewSkypeProfile(skypeProfile,itcorrespondantId); 
		
	}
	
	
	@GetMapping("/createauto")
	public void createSkypeProfilAuto() {
	//  SkypeProfileDto skypeProfile =new SkypeProfileDto("juju.titi@live.toto.com", "000015","000015");
    	
		//SkypeProfile skypeProfile =new SkypeProfile("mido.82@live.com", "000017","000016");
		//skypeProfileManagement.addNewSkypeProfile(skypeProfile);
		
		System.out.println("Exposition : création effectuée");
	
	}
	
	@GetMapping("/createautowithevent")
	public void createSkypeProfilAutoWithEvent() {
		
			
		SkypeProfileDto skypeProfile =new SkypeProfileDto("mido.82@live.com", "000017","000016");
		
		SkypeProfileEventDto skypeProfileEvent =new SkypeProfileEventDto (skypeProfile, TypeEventEnum.CREATION,"CREATION DU PROFIL SKYPE","000016");
						
	
		//skypeProfileManagement.addNewSkypeProfileWithEvent(skypeProfile,skypeProfileEvent);
		
		System.out.println("Exposition : création effectuée avec Event");
		
	
	}
	
	@PostMapping("/delete")
	public void deleteSkypeProfil(@RequestBody String sip) {


		skypeProfileManagement.deleteSkypeProfile(sip);	
		
		System.out.println("Exposition : Suppression effectuée");

	
	}
	
	@GetMapping("/list")
	public List<SkypeProfile> listItCorrespondant(){
		
		return skypeProfileManagement.findAllSkypeProfile();
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
