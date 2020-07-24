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
	public void addNewSkypeProfile(SkypeProfile skypeProfile, String itcorrespondantId) {

		repositorySkypeProfile.create(skypeProfile);

		// Ajout de l'événement correspondant à la création du profil Skype

		addNewSkypeProfileEventForCreation(skypeProfile, itcorrespondantId);
	}

	
	// Méthode permettant de créer un événement de création

	public void addNewSkypeProfileEventForCreation(SkypeProfile skypeProfile, String itCollaboraterId) {

		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();

		skypeProfileEvent.setCommentEvent("Création d'un nouveau profil Skype");

		skypeProfileEvent.setTypeEvent(TypeEventEnum.CREATION);

		skypeProfileEvent.setSkypeProfile(skypeProfile);

		// Récupération de l'It correspondant à l'origine de l'action de création

		skypeProfileEvent
				.setItCorrespondant(repositoryItCorrespondant.findItCorrespondantByCollaboraterId(itCollaboraterId));

		repositorySkypeProfileEvent.create(skypeProfileEvent);

	}

	//Methode updateSkypeProfile de mise à jour du profil Skype à partir de  :
	//@Params
	// -skypeProfileUpdated :l'objet  contenant les nouvelles valeurs
	// -itCorrespondantId   : l'identifiant du CIL réalisant l'opération
	@Override
	public void updateSkypeProfile(SkypeProfile skypeProfileUpdated, String itCorrespondantId) {
		// TODO Auto-generated method stub

		// Récupérer le profil Skype présent en base à partir du Collaborater titulaire du profil
		
		SkypeProfile skypeProfileDB = repositorySkypeProfile
				.findSkypeProfileByCollaborater(skypeProfileUpdated.getCollaborater());

		// Compléter l'objet Skypeprofil avec l'objet Collaborater
		skypeProfileUpdated.setCollaborater(skypeProfileDB.getCollaborater());

				
		// Cas de reactivation d'un profil skype Désactivé ou Expiré

		if (skypeProfileUpdated.getStatusProfile().equals("ENABLED") && 
				(skypeProfileDB.getStatusProfile().equals("DISABLED")|skypeProfileDB.getStatusProfile().equals("EXPIRED"))) {

			
			// Pour bloquer la mise à jour du profil Skype lors de la réactivation, l'objet transmis
			// est remplacé par l'objet en provenance de la base de données.
			
			skypeProfileUpdated=skypeProfileDB  ;
			
			// Mise à jour de la date d'expiration à J+2 ans
			
			skypeProfileUpdated.setExpirationDate() ;
			

		}
		
		
		// Cas de désactivation d'un profil skype
		
		if (skypeProfileUpdated.getStatusProfile().equals("DISABLED")) {
			
			// Mise à jour de la date d'expiration avec la date du jour
			
			skypeProfileUpdated.setExpirationDateToToday();

		}
		
		// Cas de mise à jour du SIP
		
		if ( (skypeProfileUpdated.getSIP()!=null) &&  (!skypeProfileUpdated.getSIP().equals(skypeProfileDB.getSIP() ) ) )  {
			
			// Mise à jour de la date d'expiration à J+2 ans
			
			skypeProfileUpdated.setExpirationDate() ;

		}
		
		
		// Mise à jour du profil Skype

		repositorySkypeProfile.update(skypeProfileDB.getSIP(), skypeProfileUpdated);

		// Création d'un événement correspondant à la mise à jour du profil Skype

		addNewSkypeProfileEventForUpdate(skypeProfileUpdated, skypeProfileDB, itCorrespondantId);
		
	
		
	}

	@Override
	public void addNewSkypeProfileEventForUpdate(SkypeProfile skypeProfileUpdated, SkypeProfile skypeProfileDB,
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

		if (!skypeProfileDB.getDialPlan().equals(skypeProfileUpdated.getDialPlan())) {
			commentForDataUpdated += "-DialPlan \n";
		}

		if (!skypeProfileDB.getExchUser().equals(skypeProfileUpdated.getExchUser())) {
			commentForDataUpdated += "-ExchUser \n";
		}
		
		if (!skypeProfileDB.getSIP().equals(skypeProfileUpdated.getSIP())) {
			commentForDataUpdated += "-SIP \n";
		}

		skypeProfileEvent.setCommentEvent(commentForDataUpdated);

		repositorySkypeProfileEvent.create(skypeProfileEvent);

	}

	@Override
	public List<SkypeProfile> findAllSkypeProfile() {

		return repositorySkypeProfile.findAllSkypeProfile();
		
	}

	@Override
	public void deleteSkypeProfile(String sip) {

		repositorySkypeProfile.delete(sip);

	}




//	@Override
//	public void addNewSkypeProfile(SkypeProfileDto skypeProfileDto) {
//
//		SkypeProfile skypeProfile = new SkypeProfile();
//		skypeProfile.setSIP(skypeProfileDto.getSIP());
//		skypeProfile.setCollaborater(repositoryCollaborater.findByCollaboraterId(skypeProfileDto.getCollaboraterId()));
//		skypeProfile.setDialPlan(skypeProfileDto.getDialPlan());
//		skypeProfile.setEnterpriseVoiceENABLED(skypeProfileDto.isEnterpriseVoiceENABLED());
//		skypeProfile.setExchUser(skypeProfileDto.getExchUser());
//		skypeProfile.setExpirationDate(skypeProfileDto.getExpirationDate());
//		skypeProfile.setExUmENABLED(skypeProfileDto.isExUmENABLED());
//		skypeProfile.setObjectClass(skypeProfileDto.getObjectClass());
//		skypeProfile.setStatusProfile(skypeProfileDto.getStatusProfile());
//		repositorySkypeProfile.create(skypeProfile);
//
//		// Ajout de l'événement correspondant à la création du profil Skype
//
//		//addNewSkypeProfileEventForCreation(skypeProfileDto);
//
//	}

//	@Override
//	public void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfileDto,
//			SkypeProfileEventDto skypeProfileEventDto) {
//
//		SkypeProfile skypeProfile = new SkypeProfile();
//
//		skypeProfile.setSIP(skypeProfileDto.getSIP());
//		skypeProfile.setCollaborater(repositoryCollaborater.findByCollaboraterId(skypeProfileDto.getCollaboraterId()));
//		skypeProfile.setDialPlan(skypeProfileDto.getDialPlan());
//		skypeProfile.setEnterpriseVoiceENABLED(skypeProfileDto.isEnterpriseVoiceENABLED());
//		skypeProfile.setExchUser(skypeProfileDto.getExchUser());
//		// skypeProfile.setExpirationDate(skypeProfileDto.getExpirationDate());
//		skypeProfile.setExUmENABLED(skypeProfileDto.isExUmENABLED());
//		skypeProfile.setObjectClass(skypeProfileDto.getObjectClass());
//		skypeProfile.setStatusProfile(skypeProfileDto.getStatusProfile());
//		repositorySkypeProfile.create(skypeProfile);
//
//		// Ajout de l'événement correspondant à la création du profil Skype
//
//		addNewSkypeProfileEvent(skypeProfileEventDto, skypeProfile);
//
//	}

//	@Override
//	public void addNewSkypeProfileEvent(SkypeProfileEventDto skypeProfileEventDto, SkypeProfile skypeProfile) {
//
//		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
//
//		skypeProfileEvent.setCommentEvent(skypeProfileEventDto.getCommentEvent());
//		skypeProfileEvent.setDateEvent(skypeProfileEventDto.getDateEvent());
//		skypeProfileEvent.setTypeEvent(skypeProfileEventDto.getTypeEvent());
//		skypeProfileEvent.setSkypeProfile(skypeProfile);
//		skypeProfileEvent.setItCorrespondant(repositoryItCorrespondant
//				.findItCorrespondantByCollaboraterId(skypeProfileEventDto.getItCorrespondantId()));
//
//		repositorySkypeProfileEvent.create(skypeProfileEvent);
//
//	}

}
