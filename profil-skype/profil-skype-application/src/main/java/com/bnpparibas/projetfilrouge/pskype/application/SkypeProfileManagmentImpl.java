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


	@Override
	public void addNewSkypeProfile(SkypeProfile skypeProfile, String itcorrespondantId) {
		
		repositorySkypeProfile.create(skypeProfile);

		// Ajout de l'événement correspondant à la création du profil Skype

		addNewSkypeProfileEventForCreation(skypeProfile,itcorrespondantId);		
	}
	

	//Méthode permetant de créer un événement de création
	
	public void addNewSkypeProfileEventForCreation(SkypeProfile skypeProfile, String itCollaboraterId) {

		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
		
		skypeProfileEvent.setCommentEvent("Création d'un nouveau profil Skype");
		
		skypeProfileEvent.setTypeEvent(TypeEventEnum.CREATION);

		skypeProfileEvent.setSkypeProfile(skypeProfile);

		//Récupération de l'It correspondant à l'origine de l'action de création
		
		skypeProfileEvent.setItCorrespondant(
				repositoryItCorrespondant.findItCorrespondantByCollaboraterId(itCollaboraterId));
 
		repositorySkypeProfileEvent.create(skypeProfileEvent);

	}
	
	@Override
	public void updateSkypeProfile(String sip, SkypeProfile skypeProfile, String itCorrespondantId) {
		
		// Récupérer le profil Skype à partir de l'identifiant SIP
		SkypeProfile skypeProfileDB = repositorySkypeProfile.findSkypeProfileBySip(sip);
		
		// Compléter l'objet Skypeprofil avec l'objet Colaborater
		skypeProfile.setCollaborater(skypeProfileDB.getCollaborater());
		
		//Vérifier les cas de demande de Désactivation ou d'Activation du profil Skype
		
			//Cas de Demande d'activation d'un profil skype déjà actif	
		
		if (skypeProfile.getStatusProfile().equals("ENABLED") &&  skypeProfileDB.getStatusProfile().equals("ENABLED")) {
			
			throw new RuntimeException("Le profil Skype :"+ skypeProfile.getSIP()+" est déja actif")  ;
		}
		
			//Cas de Demande de désactivation d'un profil skype déjà désactivé
		
		if (skypeProfile.getStatusProfile().equals("DISABLED") &&  skypeProfileDB.getStatusProfile().equals("DISABLED")) {
			
			throw new RuntimeException("Le profil Skype :"+ skypeProfile.getSIP()+" est déja désactivé")  ;
		}
		
		
			//Cas de Demande d'activation d'un profil skype déjà désactivé
		
		if (skypeProfile.getStatusProfile().equals("ENABLED") &&  skypeProfileDB.getStatusProfile().equals("DISABLED")) {
			
			//Calcul de la nouvelle date d'expiration 
			
			skypeProfile.setExpirationDate();
			
		}
		
		
		//Mise à jour du profi Skype
		
		repositorySkypeProfile.update(sip, skypeProfile);

		// Ajout de l'événement correspondant à la mise à jour du profil Skype
		 
		addNewSkypeProfileEventForUpdate(skypeProfile, skypeProfileDB, itCorrespondantId);
		
	}
	

	
	@Override
	public void addNewSkypeProfileEventForUpdate(SkypeProfile skypeProfile, SkypeProfile skypeProfileDB,
			String itCorrespondantId) {

		String commentForDataUpdated = "Mise à jour des champs : \n ";

		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();

		skypeProfileEvent.setTypeEvent(TypeEventEnum.MODIFICATION);

		skypeProfileEvent.setSkypeProfile(skypeProfile);

		skypeProfileEvent
				.setItCorrespondant(repositoryItCorrespondant.findItCorrespondantByCollaboraterId(itCorrespondantId));

		// Creation d'un commentaire dynamique contenant les différents modification
		// apportées au profil Skype

		// A faire! : alimentation dynamique.

		if (!skypeProfileDB.getDialPlan().equals(skypeProfile.getDialPlan())) {
			commentForDataUpdated += "-DialPlan \n";
		}

		if (!skypeProfileDB.getExchUser().equals(skypeProfile.getExchUser())) {
			commentForDataUpdated += "-ExchUser \n";
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

	@Override
	public void addNewSkypeProfileEvent(SkypeProfileEventDto skypeProfileEvent, SkypeProfile skypeProfile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfile, SkypeProfileEventDto skypeProfileEventDto) {
		// TODO Auto-generated method stub

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
