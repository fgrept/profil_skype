package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;



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
	 * @param eventComment
	 * @return boolean
	 */
	boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment);
	
	
	/**
	 * Méthode mettant à jour un profil Skype
	 * y compris la mise à jour de l'adresse SIP
	 * 
	 * @param skypeProfile
	 * @param idAnnuaireCIL
	 * @param eventComment
	 * @return boolean
	 */
	boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment);
	
	
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

	/**
	 * Méthode qui récupère une liste de profil Skype pour un numéro de page donné de taille donnée.
	 * Le tri peut appliqué sur un critère avec un ordre de tri.
	 * par défaut, le tri sera ascendant sur le SIP
	 * @param numberPage
	 * @param sizePage
	 * @param criteria
	 * @param boolean sort ascending
	 * @return List<SkypeProfile>
	 */
	List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria, boolean b);


	Long countSkypeProfile();


	List<SkypeProfile> findSkypeProfileWithCriteriaPage(SkypeProfile profilDom, int numberPage, int sizePage,
			String sortCriteria, boolean sortAscending);
	

	
	
}
