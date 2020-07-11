package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.bnpparibas.projetfilrouge.pskype.application.IItCorrespondantManagment;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;

@RestController
@RequestMapping("/cil")
public class ItCorrespondantController {
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantController.class);
	
	@Autowired
	private IItCorrespondantManagment correspondantManagement;

	@PostMapping("create")
	@ResponseStatus(HttpStatus.CREATED)
	public void createCIL(@RequestBody ItCorrespondant itCorrespondant) {
		correspondantManagement.createCIL(itCorrespondant.getLastNamePerson(), itCorrespondant.getLastNamePerson(), itCorrespondant.getCollaboraterId(), 
				itCorrespondant.getDeskPhoneNumber(), itCorrespondant.getMobilePhoneNumber(), itCorrespondant.getMailAdress());
	}
	
	@GetMapping("createauto")
	public String CreateAutoCIL() {
		
//		ItCorrespondant itCorrespondant = new ItCorrespondant("Grept", "Fabien", "116453", "0140401234", "0640140102", "fabien.toto@gmail.com");
		correspondantManagement.createCIL("Grept", "Fabien", "000001", "0140401234", "0640140102", "fabien.toto@gmail.com");
		correspondantManagement.createCIL("ElOuarak", "Mehdi", "000002", "0140402345", "0640140304", "mehdi.titi@gmail.com");
		correspondantManagement.createCIL("Tige", "Judicael", "000003", "0140403456", "0640140405", "judi.tata@gmail.com");	
		return "creation effectuée";
	}
	
	@GetMapping("list")
	public List<ItCorrespondant> listItCorrespondant(){
		
		return correspondantManagement.listItCorrespondant();
	}
	
}
