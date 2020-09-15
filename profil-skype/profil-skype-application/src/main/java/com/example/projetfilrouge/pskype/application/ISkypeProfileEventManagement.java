package com.example.projetfilrouge.pskype.application;

import java.util.List;


import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfileEvent;

/**
 * 
 * Liste des méthodes disponibles sur la couche exposition concernant les évènements
 * - US003 : consulter la liste des évènements d'un profil skype
 *
 */
public interface ISkypeProfileEventManagement {
	
	/**
	 * Méthode permettant de récupérer tous les évènements associés à un profil Skype
	 * 
	 * @param SIP
	 * @return List<SkypeProfileEvent>
	 */
	public List<SkypeProfileEvent> getAllEventFromSkypeProfil (String SIP);

}
