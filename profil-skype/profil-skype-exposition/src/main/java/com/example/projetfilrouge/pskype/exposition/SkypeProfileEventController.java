package com.example.projetfilrouge.pskype.exposition;

import java.util.ArrayList;
import java.util.List;

import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projetfilrouge.pskype.application.ISkypeProfileEventManagement;
import com.example.projetfilrouge.pskype.dto.SkypeProfileEventDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.server.ResponseStatusException;

/**
 * Classe exposant les API rest sur les évènements associés au profils Skype
 * Elle comprend uniquement la possibilté de récupérer les évènements associés
 * 
 * @author 116453
 *
 */

@RestController
@RequestMapping("/v1/events")
@Secured("ROLE_USER")
@Api(value = "Skype profile event REST Controller : contient toutes les opérations pour manager les événements d'un profil skype")
public class SkypeProfileEventController {
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileEventController.class);
	
	@Autowired
	ISkypeProfileEventManagement skypeProfileEventManagement;
	
	@GetMapping("/list/{sip}")
	@ApiOperation(value = "Récupère l'ensemble des événements d'un profil skype")
	@ApiResponse(code = 200,message ="Ok, liste retournée")
	public ResponseEntity<List<SkypeProfileEventDto>> getEventsFromProfil (@PathVariable("sip") String SIP) {
		List<SkypeProfileEvent> listEvents;
		try {
			listEvents = skypeProfileEventManagement.getAllEventFromSkypeProfil(SIP);
		}
	 	catch (NotFoundException e) {
			String msg = "exception déclenchée : " + e.getCode() + " - " + e.getMessage();
			logger.error(msg);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg, e);
		}
		List<SkypeProfileEventDto> listEventsDto = new ArrayList<SkypeProfileEventDto>();
		
		if (listEvents == null) {
			logger.debug("aucun évènement trouvé pour le profil skype demandé");
			return new ResponseEntity<List<SkypeProfileEventDto>>(listEventsDto, HttpStatus.OK);
		}

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
		String firstName;
		String lastName;
		String collaboraterId;
		if (eventDom.getItCorrespondant() == null) {
			logger.error("It correspondant non trouvé");
			firstName="";
			lastName="";
			collaboraterId="";
		}else {
			firstName=eventDom.getItCorrespondant().getFirstNamePerson();
			lastName=eventDom.getItCorrespondant().getLastNamePerson();
			collaboraterId=eventDom.getItCorrespondant().getCollaboraterId();
		}
		return new SkypeProfileEventDto(
				eventDom.getDateEvent(), eventDom.getTypeEvent(), eventDom.getCommentEvent(),
				collaboraterId,
				firstName,
				lastName);

	}

}
