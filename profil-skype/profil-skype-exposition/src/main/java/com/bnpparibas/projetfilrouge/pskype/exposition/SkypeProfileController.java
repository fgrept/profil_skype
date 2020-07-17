package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ISkypeProfileEventManagement;
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
public class SkypeProfileController {

	@Autowired
	private ISkypeProfileManagement skypeProfileManagement;
	
	
	@PostMapping("/create")
	public void createSkypeProfil(@RequestBody SkypeProfileDto skypeProfile,SkypeProfileEventDto skypeProfileEventDto) {
		skypeProfileManagement.addNewSkypeProfileWithEvent(skypeProfile,skypeProfileEventDto);
	}
	
	@GetMapping("/createauto")
	public void createSkypeProfilAuto() {
		
		SkypeProfileDto skypeProfile =new SkypeProfileDto("mido.82@live.com", "000017","000016");
		skypeProfileManagement.addNewSkypeProfile(skypeProfile);		
		System.out.println("Exposition : création effectuée");

	
	}
	
	@GetMapping("/createautowithevent")
	public void createSkypeProfilAutoWithEvent() {
		
		System.out.println("Début Event");
		
		SkypeProfileDto skypeProfile =new SkypeProfileDto("mido.82@live.com", "000017","000016");
		
		SkypeProfileEventDto skypeProfileEvent =new SkypeProfileEventDto (skypeProfile, TypeEventEnum.CREATION,"CREATION DU PROFIL SKYPE","000017");
		
				
		System.out.println("it_correspondant_id_user :" + skypeProfileEvent.getItCorrespondantId()  );
		
		skypeProfileManagement.addNewSkypeProfileWithEvent(skypeProfile,skypeProfileEvent);
		
		System.out.println("Exposition : création effectuée avec Event");
		
	
	}
	
	@PostMapping("/delete")
	public void deleteSkypeProfil(@RequestBody String sip) {
		
		System.out.println(sip ) ;
		
		skypeProfileManagement.deleteSkypeProfile(sip);	
		
		System.out.println("Exposition : Suppression effectuée");

	
	}
	
	@GetMapping("/list")
	public List<SkypeProfile> listItCorrespondant(){
		
		return skypeProfileManagement.findAllSkypeProfile();
	}
}
