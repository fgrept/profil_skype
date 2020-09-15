package com.example.projetfilrouge.pskype.domain.skypeprofile;

import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;

import java.util.List;
/**
 * Interface Skype Profil Event de la couche domaine
 */
public interface ISkypeProfileEventDomain {

	/**
	 * Creation d'un événement relatif à la création d'un profil skype
	 * @param skypeProfileEvent
	 */
	void create(SkypeProfileEvent skypeProfileEvent);
	
	/**
	 * Creation d'un événement relatif à la suppression d'un profil skype
	 * @param skypeProfileEvent
	 */
	void delete(SkypeProfileEvent skypeProfileEvent);
	
	/**
	 * suppression de tous les événements d'un profil skype
	 * @param skypeProfile
	 */
	void deleteAllEventByProfile(SkypeProfile skypeProfile);
	/**
	 * Lister tous les événements d'un profil skype
	 * @param skypeProfile
	 * @return une liste d'événements
	 */
	List<SkypeProfileEvent> findAllEventBySkypeProfile(SkypeProfile skypeProfile);
	
	/**
	 * récupérer les événements relatifs à un It Correspondant
	 * @param collaboraterId
	 * @return une liste d'événements
	 */
	List<SkypeProfileEvent> findAllEventByItCorrespondantId(String collaboraterId);
	
	/**
	 * mise à jour 
	 * @param itCorrespondant
	 * @param itCorrespondantNew
	 * @return un boolean
	 */
	boolean updateEventItCorrespondant(ItCorrespondant itCorrespondant, ItCorrespondant itCorrespondantNew);
} 
