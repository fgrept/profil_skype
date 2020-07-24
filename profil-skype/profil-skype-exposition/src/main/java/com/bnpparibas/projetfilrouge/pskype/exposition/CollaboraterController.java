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

/**
 * 
 * @author Judicaël
 *
 */
@RestController
@RequestMapping("/collaborater")
@Secured({"ROLE_RESP","ROLE_ADMIN"})
public class CollaboraterController {
	
	private static Logger logger = LoggerFactory.getLogger(CollaboraterController.class);
	
	@Autowired
	private ICollaboraterManagment collaboraterManagment;
	
	@GetMapping("/list")
	public ResponseEntity<List<CollaboraterDto>> listCollaborater(){
		
		List<Collaborater> list =  collaboraterManagment.listCollaborater();
		List<CollaboraterDto> dto = new ArrayList<CollaboraterDto>();
		for (Collaborater collaborater:list) {
			dto.add(mapperDomainToDto(collaborater));
		}
//		 new ResponseEntity<List<Collaborater>>(collaboraterManagment.);
		return new ResponseEntity<List<CollaboraterDto>>(dto,HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Boolean> createCollaborater(@RequestBody CollaboraterDto dto) {
		
		if (dto.getCollaboraterId() == null) {
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
