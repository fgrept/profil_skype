package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.bnpparibas.projetfilrouge.pskype.application.IItCorrespondantManagment;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.ItCorrespondantDto;
/**
 * Classe exposant des API rest dédiées à l'itCorrespondant
 * @author Judicaël
 *
 */
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
//		correspondantManagement.createCIL("Grept", "Fabien", "000004", "0140401234", "0640140102", "fabien.toto@gmail.com");
//		correspondantManagement.createCIL("ElOuarak", "Mehdi", "000005", "0140402345", "0640140304", "mehdi.titi@gmail.com");
//		correspondantManagement.createCIL("Tige", "Judicael", "000006", "0140403456", "0640140405", "judi.tata@gmail.com");	
		correspondantManagement.createCIL("Tige", "Judi", "000016", "0140403456", "0640140405", "judi.tata@gmail.com");
		correspondantManagement.createCIL("Tige", "Juju", "000017", "0140403456", "0640140405", "judi.tata@gmail.com");
		correspondantManagement.createCIL("Tige", "Toto", "000018", "0140403456", "0640140405", "judi.tata@gmail.com");
		return "creation effectuée";
	}
	
	@GetMapping("list")
	public List<ItCorrespondant> listItCorrespondant(){
		
		return correspondantManagement.listItCorrespondant();
	}
	/**
	 * 1ère version de la recherche du CIL avec filtre limité (requetage non dynamique)
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @return Liste des IT Correspondant
	 */
	@GetMapping("list/{id}/{lastName}/{firstName}")
	public List<ItCorrespondant> listItCorrespondantByIdAndName(@PathVariable("id") String id, @PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName){
		
		System.out.println("id "+id);
		System.out.println("lastName "+lastName);
		System.out.println("firstName "+firstName);
		
		return correspondantManagement.listItCorrespondantFilters(id, lastName, firstName,"","","");
	}
	
	/**
	 * Version finale de la recherche d'un CIL (pas de recherche sur l'UO pour le moment)
	 * @param itCorrespondant
	 * @return Liste des IT Correspondant
	 */
	@GetMapping("listfilter")
	public List<ItCorrespondant> listItCorrespondantFilters(@RequestBody ItCorrespondantDto itCorrespondant){
		
		return correspondantManagement.listItCorrespondantFilters(itCorrespondant.getCollaboraterId(), itCorrespondant.getLastName(), itCorrespondant.getFirstName(),
				itCorrespondant.getDeskPhoneNumber(),itCorrespondant.getMobilePhoneNumber(),itCorrespondant.getMailAdress());
		
	}
	@GetMapping("uprole/{id}/{role}")
	public void updateRoleItCorrespondant(@PathVariable("id") String id, @PathVariable("role") String role) {
		
		System.out.println("id en entree " + id);
		System.out.println("role en entree " + role);
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		
		if (role.equals("admin")) {
			correspondantManagement.updateRoleCIL(id, roles);
			System.out.println("Passage à role ADMIN");
			roles.add(RoleTypeEnum.ROLE_ADMIN);
			roles.add(RoleTypeEnum.ROLE_RESP);
			roles.add(RoleTypeEnum.ROLE_USER);
		}
		if (role.equals("resp")) {
			correspondantManagement.updateRoleCIL(id, roles);
			System.out.println("Passage à role RESP");
			roles.add(RoleTypeEnum.ROLE_RESP);
			roles.add(RoleTypeEnum.ROLE_USER);
		}
		if (role.equals("user")) {
			correspondantManagement.updateRoleCIL(id, roles);
			System.out.println("Passage à role USER");
			roles.add(RoleTypeEnum.ROLE_USER);
		}
		
		System.out.println("Mise à jour effectuée");
//		return correspondantManagement.listItCorrespondantFilters(id, "", "", "", "", "").get(0);
	}
	
}
