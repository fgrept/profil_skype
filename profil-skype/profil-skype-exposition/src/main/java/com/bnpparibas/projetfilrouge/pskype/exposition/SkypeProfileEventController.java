package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ISkypeProfileEventManagement;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Classe exposant les API rest sur les évènements associés au profils Skype
 * Elle comprend uniquement la possibilté de récupérer les évènements associés
 * 
 * @author 116453
 *
 */

@RestController
@RequestMapping("/events")
@Secured("ROLE_USER")
public class SkypeProfileEventController {
	
	@Autowired
	ISkypeProfileEventManagement skypeProfileEventManagement;
	
	@GetMapping("/list/{sip}")
	public ResponseEntity<List<SkypeProfileEventDto>> getEventsFromProfil (@PathVariable("sip") String SIP) {
		
		List<SkypeProfileEvent> listEvents = skypeProfileEventManagement.getAllEventFromSkypeProfil(SIP);
		
		List<SkypeProfileEventDto> listEventsDto = new ArrayList<SkypeProfileEventDto>();
		for (SkypeProfileEvent skypeProfileEvent : listEvents) {
			listEventsDto.add(mapperDomaintoDto(skypeProfileEvent));
		}
		return new ResponseEntity<List<SkypeProfileEventDto>>(listEventsDto, HttpStatus.OK);
		
	}
	
	/**
	 * Mapper du Domain vers le dto exposé à l'utilisateur
	 * @param eventDom
	 * @return eventDto
	 */
	private SkypeProfileEventDto mapperDomaintoDto (SkypeProfileEvent eventDom) {
		
		SkypeProfileEventDto profilDto = new SkypeProfileEventDto(
				eventDom.getDateEvent(), eventDom.getTypeEvent(), eventDom.getCommentEvent(),
				eventDom.getItCorrespondant().getCollaboraterId(),
				eventDom.getItCorrespondant().getFirstNamePerson(),
				eventDom.getItCorrespondant().getLastNamePerson());

		return profilDto;
	}

}
