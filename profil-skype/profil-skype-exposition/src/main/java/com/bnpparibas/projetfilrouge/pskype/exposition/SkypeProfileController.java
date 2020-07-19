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
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;

/**
 * Classe exposant des API rest dédiées à profil Skype
 * @author Judicaël
 *
 */
@RestController
@RequestMapping("/profile")
@Secured({"ROLE_USER","ROLE_RESP","ROLE_ADMIN"})
public class SkypeProfileController {

	@Autowired
	private ISkypeProfileManagement skypeProfileManagement;
	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@PostMapping("/create")
	public void createSkypeProfil(@RequestBody SkypeProfileDto skypeProfile) {
		skypeProfileManagement.addNewSkypeProfile(skypeProfile);
	}
	
	@Secured({"ROLE_RESP","ROLE_ADMIN"})
	@GetMapping("/createauto")
	public void createSkypeProfilAuto() {
		SkypeProfileDto skypeProfile =new SkypeProfileDto("juju.titi@live.toto.com", "000015","000015");
		skypeProfileManagement.addNewSkypeProfile(skypeProfile);
		System.out.println("Exposition : création effectuée");
		
	}
	
	@GetMapping("/list")
	public List<SkypeProfile> listItCorrespondant(){
		
		return skypeProfileManagement.findAllSkypeProfile();
	}
}
