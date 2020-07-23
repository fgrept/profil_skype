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
 * @author Judicaël.
 *
 */
@Service
@Transactional
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement,ISkypeProfileEventManagement  {

	
	@Autowired
	private ISkypeProfileDomain repositorySkypeProfile;
	
	@Autowired 
	private ICollaboraterDomain repositoryCollaborater;
	
	@Autowired
	private IItCorrespondantDomain repositoryItCorrep;
	
	@Autowired
	private ISkypeProfileEventDomain repositorySkypeProfileEvent;
	
	@Autowired 
	private IItCorrespondantDomain repositoryItCorrespondant;
	
	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {
		// TODO Auto-generated method stub
		
		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	
	@Override
	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL) {
		
		 if (repositorySkypeProfile.create(skypeProfile) == true) {
			 ItCorrespondant itCorrespondant = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
			 
			 // création de l'évènement associé
			 if (itCorrespondant != null) {
				 SkypeProfileEvent event = new SkypeProfileEvent("création du profil", skypeProfile, itCorrespondant, TypeEventEnum.CREATION);
				 repositorySkypeProfileEvent.create(event);
				 return true;
			}
		} 
		 return false;
	}
	
	

	@Override
	@Deprecated
	// à merger une fois les modifications de Mehdi faites
	public void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfileDto,SkypeProfileEventDto skypeProfileEventDto ) {
		

		SkypeProfile skypeProfile = new SkypeProfile();
		
		skypeProfile.setSIP(skypeProfileDto.getSIP());		
		skypeProfile.setCollaborater(repositoryCollaborater.findByCollaboraterId(skypeProfileDto.getCollaboraterId()));	
		skypeProfile.setDialPlan(skypeProfileDto.getDialPlan());
		skypeProfile.setEnterpriseVoiceEnabled(skypeProfileDto.isEnterpriseVoiceEnabled());
		skypeProfile.setExchUser(skypeProfileDto.getExchUser());
		//skypeProfile.setExpirationDate(skypeProfileDto.getExpirationDate());
		skypeProfile.setExUmEnabled(skypeProfileDto.isExUmEnabled());
		skypeProfile.setObjectClass(skypeProfileDto.getObjectClass());
		skypeProfile.setStatusProfile(skypeProfileDto.getStatusProfile());
		repositorySkypeProfile.create(skypeProfile);
		
 				
		//Ajout de l'événement correspondant à la création du profil Skype
		
		addNewSkypeProfileEvent(skypeProfileEventDto,skypeProfile);
		
	}

	@Override
	public List<SkypeProfile> findAllSkypeProfile() {
		
		return repositorySkypeProfile.findAllSkypeProfile();
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
	@Deprecated
	// à merger une fois les modifications de Mehdi faites
	public void addNewSkypeProfileEvent(SkypeProfileEventDto skypeProfileEventDto, SkypeProfile skypeProfile) {

	
			
		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
		 
		skypeProfileEvent.setCommentEvent(skypeProfileEventDto.getCommentEvent());
		skypeProfileEvent.setDateEvent(skypeProfileEventDto.getDateEvent());
		skypeProfileEvent.setTypeEvent(skypeProfileEventDto.getTypeEvent());
		skypeProfileEvent.setSkypeProfile(skypeProfile);
		skypeProfileEvent.setItCorrespondant(repositoryItCorrespondant.findItCorrespondantByCollaboraterId(skypeProfileEventDto.getItCorrespondantId()));
		
		//TESTMHD
		
		System.out.println ("Récupération de ItCorrespondant" + skypeProfileEvent.getItCorrespondant().getCollaboraterId() ) ;
		System.out.println ("L'objet ItCorrespondant" + skypeProfileEvent.getItCorrespondant() ) ;
		
		repositorySkypeProfileEvent.create(skypeProfileEvent);
             	
		
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


	@Override
	public boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL) {
		
		// Lors de la mise à jour d'un profil : tous les champs peuvent être modifiés
		// On récupère donc le profil existant associé au collaborateur
		// La date d'expiration doit aussi être récupérée car non modifiable par les users
		SkypeProfile profilExisting = findSkypeProfilFromCollab(skypeProfile.getCollaborater().getCollaboraterId());
		
		// RG à coder sur les statut, date, ...
		// entre profil existant et profil reçu à modifier
		
		boolean isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
		
		// Récupération du CIL demandant la modif
		ItCorrespondant cilRequester = repositoryItCorrep.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
		
		if (cilRequester != null && isUpdatedProfil) {
			SkypeProfileEvent event = new SkypeProfileEvent("mise à jour", skypeProfile, cilRequester, TypeEventEnum.MODIFICATION);
			return true;
		}	
		
		return false;
	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}

}
