package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;

/**
 * 
 * Liste des méthodes disponibles sur la couche exposition concernant les évènements
 * - ajouter un évènement à un profil skype
 * - US003 : consulter la liste des évènements d'un profil skype
 *
 */
public interface ISkypeProfileEventManagement {
	
	void addNewSkypeProfileEvent(SkypeProfileEventDto skypeProfileEvent,SkypeProfile skypeProfile);
	
	/**
	 * Méthode permettant de récupérer tous les évènements associés à un profil Skype
	 * 
	 * @param SIP
	 * @return List<SkypeProfileEvent>
	 */
	public List<SkypeProfileEvent> getAllEventFromSkypeProfil (String SIP);
	
}
