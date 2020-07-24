package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Services dédiées au profil skype
 * 
 * @author 479680.
 *
 */
@Service
@Transactional
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement, ISkypeProfileEventManagement {

	@Autowired
	private ISkypeProfileDomain repositorySkypeProfile;


	@Autowired
	private ISkypeProfileEventDomain repositorySkypeProfileEvent;

	@Autowired
	ICollaboraterDomain repositoryCollaborater;

	@Autowired
	IItCorrespondantDomain repositoryItCorrespondant;
	
	
	@Autowired
	private IItCorrespondantDomain repositoryItCorrep;
	

	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {
		// TODO Auto-generated method stub

		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	//Methode addNewSkypeProfile de création d'un nouveau du profil Skype à partir de  :
	//@Params
	// -skypeProfile :l'objet  contenant l'objet skype profil à créer
	// -itCorrespondantId   : l'identifiant du CIL réalisant l'opération
	
	@Override
	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL) {

		repositorySkypeProfile.create(skypeProfile);

		// Ajout de l'événement correspondant à la création du profil Skype
		// Méthode permettant de créer un événement de création
		
		if (repositorySkypeProfile.create(skypeProfile) == true) {
			addNewSkypeProfileEventForCreation(skypeProfile, idAnnuaireCIL);
		}
				
		return true ;
		
	}
	
//  !!!! CODE DE FABIEN A VOIR //		
//	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL) {
//		
//		 if (repositorySkypeProfile.create(skypeProfile) == true) {
//			 ItCorrespondant itCorrespondant = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
//			 
//			 // création de l'évènement associé
//			 if (itCorrespondant != null) {
//				 SkypeProfileEvent event = new SkypeProfileEvent("création du profil", skypeProfile, itCorrespondant, TypeEventEnum.CREATION);
//				 repositorySkypeProfileEvent.create(event);
//				 return true;
//			}
//		} 
//		 return false;
//		 
//	}

	public void addNewSkypeProfileEventForCreation(SkypeProfile skypeProfile, String idAnnuaireCIL) {

		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();

		skypeProfileEvent.setCommentEvent("Création d'un nouveau profil Skype");

		skypeProfileEvent.setTypeEvent(TypeEventEnum.CREATION);

		skypeProfileEvent.setSkypeProfile(skypeProfile);

		// Récupération de l'It correspondant à l'origine de l'action de création

		skypeProfileEvent
				.setItCorrespondant(repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL));

		repositorySkypeProfileEvent.create(skypeProfileEvent);

	}

	
	
	//Methode updateSkypeProfile de mise à jour du profil Skype à partir de  :
	//@Params
	// -skypeProfileUpdated :l'objet  contenant les nouvelles valeurs
	// -itCorrespondantId   : l'identifiant du CIL réalisant l'opération
	
	
	
	@Override
	public boolean updateSkypeProfile(SkypeProfile skypeProfileUpdated, String idAnnuaireCIL) {

		StatusSkypeProfileEnum statusProfile = null;
		
		// Récupérer le profil Skype présent en base à partir du Collaborater titulaire du profil

		
		SkypeProfile skypeProfileExisting = repositorySkypeProfile
				.findSkypeProfileByCollaborater(skypeProfileUpdated.getCollaborater());

		// Compléter l'objet Skypeprofil avec l'objet Collaborater
		skypeProfileUpdated.setCollaborater(skypeProfileExisting.getCollaborater());

				
		// Cas de reactivation d'un profil skype Désactivé ou Expiré

		if (skypeProfileUpdated.getStatusProfile().equals("ENABLED") && 
				(skypeProfileExisting.getStatusProfile().equals("DISABLED")|skypeProfileExisting.getStatusProfile().equals("EXPIRED"))) {

			
			// Pour bloquer la mise à jour du profil Skype lors de la réactivation, l'objet transmis
			// est remplacé par l'objet en provenance de la base de données.
			
			skypeProfileUpdated=skypeProfileExisting  ;
			
			// Mise à jour de la date d'expiration à J+2 ans
			
			skypeProfileUpdated.setExpirationDate() ;
			
			//Changement du status à ENABLED
			skypeProfileUpdated.setStatusProfile(statusProfile.ENABLED);

		}
		

		// Cas de désactivation d'un profil skype
		
		if (skypeProfileUpdated.getStatusProfile().equals("DISABLED")) {
			
			// Mise à jour de la date d'expiration avec la date du jour
			
			skypeProfileUpdated.setExpirationDateToToday();
			
			//Changement du status à DISABLED
								
			skypeProfileUpdated.setStatusProfile(statusProfile.DISABLED);

		}
		
		// Cas de mise à jour du SIP
		
		if ( (skypeProfileUpdated.getSIP()!=null) &&  (!skypeProfileUpdated.getSIP().equals(skypeProfileExisting.getSIP() ) ) )  {
			
			// Mise à jour de la date d'expiration à J+2 ans
			
			skypeProfileUpdated.setExpirationDate() ;

		}
		
		
		// Mise à jour du profil Skype

		repositorySkypeProfile.update(skypeProfileUpdated);

		
		if (repositorySkypeProfile.update(skypeProfileUpdated) == true) {
			// Création d'un événement correspondant à la mise à jour du profil Skype

			addNewSkypeProfileEventForUpdate(skypeProfileUpdated, skypeProfileExisting, idAnnuaireCIL);
		}
				
		return true;
		

	}
	

	@Override

	public void addNewSkypeProfileEventForUpdate(SkypeProfile skypeProfileUpdated, SkypeProfile skypeProfileExisting,
			String itCorrespondantId) {

		String commentForDataUpdated = "Mise à jour des champs : \n ";

		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();

		skypeProfileEvent.setTypeEvent(TypeEventEnum.MODIFICATION);

		skypeProfileEvent.setSkypeProfile(skypeProfileUpdated);

		skypeProfileEvent
				.setItCorrespondant(repositoryItCorrespondant.findItCorrespondantByCollaboraterId(itCorrespondantId));

		// Creation d'un commentaire dynamique contenant les différents modification
		// apportées au profil Skype

		// A faire! : alimentation dynamique via une boucle (à vérifier).

		if (!skypeProfileExisting.getDialPlan().equals(skypeProfileUpdated.getDialPlan())) {
			commentForDataUpdated += "-DialPlan \n";
		}

		if (!skypeProfileExisting.getExchUser().equals(skypeProfileUpdated.getExchUser())) {
			commentForDataUpdated += "-ExchUser \n";
		}
		
		if (!skypeProfileExisting.getSIP().equals(skypeProfileUpdated.getSIP())) {
			commentForDataUpdated += "-SIP \n";
		}

		skypeProfileEvent.setCommentEvent(commentForDataUpdated);

		repositorySkypeProfileEvent.create(skypeProfileEvent);

	}

	
	/**
	 * Cette méthode accède directement au REPO
	 * C'est le REPO qui fera la vérification avant suppression pour récupérer l'id entity
	 * et éviter un double appel
	 * 
	 */
	@Override
	public boolean deleteSkypeProfile(String sip) {
		
		return repositorySkypeProfile.delete(sip);
		
	}
	
	@Override
	public List<SkypeProfile> findAllSkypeProfile() {

		return repositorySkypeProfile.findAllSkypeProfile();
		
	}


	@Override
	public List<SkypeProfileEvent> getAllEventFromSkypeProfil(String SIP) {
		
		SkypeProfile profil = repositorySkypeProfile.findSkypeProfileBySip(SIP);
		if (profil == null) {
			throw new RuntimeException("Skype profil : " + SIP + "non trouvé en base");
		} 
		else {
			return repositorySkypeProfileEvent.findAllEventBySkypeProfile(profil);
		}

	}


	@Override
	public List<SkypeProfile> findSkypeProfileWithCriteria(SkypeProfile profil) {
		
		
		return repositorySkypeProfile.findAllSkypeProfileFilters(profil.isEnterpriseVoiceEnabled(), profil.getVoicePolicy(),
				profil.getDialPlan(), profil.getSamAccountName(), profil.isExUmEnabled(),
				profil.getExchUser(), profil.getStatusProfile(), profil.getCollaborater().getOrgaUnit().getOrgaUnityCode(),
				profil.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode());
	}

//  !!!! CODE DE FABIEN A VOIR //	
//	@Override
//	public boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL) {
//		
//		// Lors de la mise à jour d'un profil : tous les champs peuvent être modifiés
//		// On récupère donc le profil existant associé au collaborateur
//		// La date d'expiration doit aussi être récupérée car non modifiable par les users
//		SkypeProfile profilExisting = findSkypeProfilFromCollab(skypeProfile.getCollaborater().getCollaboraterId());
//		
//		// RG à coder sur les statut, date, ...
//		// entre profil existant et profil reçu à modifier
//		
//		boolean isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
//		
//		// Récupération du CIL demandant la modif
//		ItCorrespondant cilRequester = repositoryItCorrep.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
//		
//		if (cilRequester != null && isUpdatedProfil) {
//			SkypeProfileEvent event = new SkypeProfileEvent("mise à jour", skypeProfile, cilRequester, TypeEventEnum.MODIFICATION);
//			return true;
//		}	
//		
//		return false;
//	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}


}
