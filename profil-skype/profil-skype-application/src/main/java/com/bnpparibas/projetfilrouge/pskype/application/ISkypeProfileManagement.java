package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Classe exposant les services relatifs aux profils Skype
 * 
 * @author La Fabrique
 *
 */
public interface ISkypeProfileManagement {
	
	/**
	 * Méthode permettant de récupérer le profil Skype actif en fonction d'un SIP donné
	 * 
	 * @param sip
	 * @return SkypeProfile
	 */
	SkypeProfile consultActiveSkypeProfile(String sip);
	
	
	/**
	 * Méthode permettant de récupérer le profilSkype d'un collaborateur quelque soit son statut
	 * Utilisé pour tous les cas de mise à jour du profil qui peuvent concerner tous les champs, même le SIP
	 * 
	 * @param idAnnuaire
	 * @return SkypeProfile
	 */
	SkypeProfile findSkypeProfilFromCollab (String idAnnuaire);
	
	
	/**
	 * Méthode permettant de créer un profil skype pour un collaborateur
	 * qui n'en a jamais eu
	 * 
	 * @param skypeProfile
	 * @param idAnnuaireCIL
	 * @return boolean
	 */
	boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL);
	
	
	/**
	 * Méthode mettant à jour un profil Skype
	 * y compris la mise à jour de l'adresse SIP
	 * 
	 * @param skypeProfile
	 * @param idAnnuaireCIL
	 * @return boolean
	 */
	boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL);
	
	
	//à merger avec méthode ci-dessus pour inclure les RDG de Mehdi
	@Deprecated
	void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfile, SkypeProfileEventDto skypeProfileEventDto);
	
	
	/**
	 * Méthode récupérant tous les profils skype de la base
	 * (page d'accueil sans critère de recherche)
	 * 
	 * @return List<SkypeProfile>
	 */
	List<SkypeProfile> findAllSkypeProfile();
	
	
	/**
	 * Méthode permettant de récupérer les profils Skype correspondant
	 * à certains critères : profil, uo, site.
	 * 
	 * @param skypeProfil
	 * @return List<SkypeProfile>
	 */
	List<SkypeProfile> findSkypeProfileWithCriteria(SkypeProfile skypeProfil);
	
	
	boolean deleteSkypeProfile(String sip);
}
