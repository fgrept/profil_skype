package com.example.projetfilrouge.pskype.application;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.projetfilrouge.pskype.domain.collaborater.ICollaboraterDomain;
import com.example.projetfilrouge.pskype.domain.skypeprofile.*;
import com.example.projetfilrouge.pskype.domain.user.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.example.projetfilrouge.pskype.domain.exception.NotAuthorizedException;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;

/**
 * Services dédiées au profil skype
 * 
 * @author 479680.
 *
 */
@Service
@Transactional
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement, ISkypeProfileEventManagement {

	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileManagmentImpl.class);
	
	@Autowired
	private ISkypeProfileDomain repositorySkypeProfile;


	@Autowired
	private ISkypeProfileEventDomain repositorySkypeProfileEvent;

	@Autowired
	ICollaboraterDomain repositoryCollaborater;
	
	@Autowired 
	private IItCorrespondantDomain repositoryItCorrespondant;
	

	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {

		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	@Override
	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment) {
		
		
		ItCorrespondant itCorrespondant = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
			 
		if (itCorrespondant != null) {
			// création de l'évènement associé
			if (repositorySkypeProfile.create(skypeProfile)) {
				 SkypeProfileEvent event = new SkypeProfileEvent(eventComment, skypeProfile, itCorrespondant, TypeEventEnum.CREATION);
				 repositorySkypeProfileEvent.create(event);
				 return true;
			}else {
				if (logger.isErrorEnabled()){
					String msg = "Profil skype non créé :"+skypeProfile.getSIP();
					logger.error(msg);
				}
				return false;
			}
		}else {
			if (logger.isErrorEnabled()){
				String msg = "Pas d'it correspondant trouvé pour id annuaire :"+idAnnuaireCIL;
				logger.error(msg);
			}
			return false;
		}
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
		
		List<SkypeProfileEvent> listEvents;
		
		SkypeProfile profil = repositorySkypeProfile.findSkypeProfileBySip(SIP);
		if (profil == null) {
			String msg = "Profil Skype : " + SIP + " non trouvé en base";
			if (logger.isErrorEnabled()){
				logger.error(msg);
			}
			throw new NotFoundException(ExceptionListEnum.NOTFOUND12, msg);
		} 
		else {
			listEvents = repositorySkypeProfileEvent.findAllEventBySkypeProfile(profil);
		}
		return listEvents;
	}


	@Override
	public List<SkypeProfile> findSkypeProfileWithCriteria(SkypeProfile profil) {
		
		return repositorySkypeProfile.findAllSkypeProfileFilters(profil.isEnterpriseVoiceEnabled(), profil.getVoicePolicy(),
				profil.getDialPlan(), profil.getSamAccountName(), profil.isExUmEnabled(),
				profil.getExchUser(), profil.getStatusProfile(), profil.getCollaborater().getOrgaUnit().getOrgaUnityCode(),
				profil.getCollaborater().getOrgaUnit().getOrgaSite().getSiteCode(),
				profil.getCollaborater().getLastNamePerson(),
				profil.getCollaborater().getFirstNamePerson());
	}
	

	@Override
	public List<SkypeProfile> findSkypeProfileWithCriteriaPage(SkypeProfile profilDom, int numberPage, int sizePage,
			String sortCriteria, boolean sortAscending) {
		
		return repositorySkypeProfile.findAllSkypeProfileFiltersPage(profilDom,numberPage,sizePage,sortCriteria,sortAscending);
	}

	@Override
	public boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment) {
		
		boolean isUpdatedProfil = false;
		List<String> changedFields;
		String comment = "Commentaire utilisateur : <<" + eventComment + ">>";
		
		// Lors de la mise à jour d'un profil : tous les champs peuvent être modifiés
		// On récupère donc le profil existant associé au collaborateur
		SkypeProfile profilExisting = findSkypeProfilFromCollab(skypeProfile.getCollaborater().getCollaboraterId());		
		if (profilExisting == null) {
			String msg = "Pas de profil skype pour ce collaborateur : " + skypeProfile.getCollaborater().getCollaboraterId();
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND3, msg);
		}

		// Récupération du CIL demandant la modif
		ItCorrespondant cilRequester = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);		
		if (cilRequester == null) {
			String msg = "cil non trouvé, id annuaire: "+idAnnuaireCIL;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND4, msg);
		}
		
		// Contrôle du statut du profil à mettre à jour(Impossible de mettre à jour un profil à expiré depuis le front)		
		if (skypeProfile.getStatusProfile() == StatusSkypeProfileEnum.EXPIRED) {
			String msg = "mise à jour d'un profil à expiré";
			logger.error(msg);
			throw new NotAuthorizedException(ExceptionListEnum.NOTAUTHORIZED1, msg);
		}
		
		
	
				
		// 1) Cas de la modification d'adresse SIP => création d'un nouveau profil
		if (!(skypeProfile.getSIP().equals(profilExisting.getSIP()))) {
			// interdit de créer un nouveau profil autrement que actif
			if (skypeProfile.getStatusProfile() != StatusSkypeProfileEnum.ENABLED) {
				String msg = "création d'un profil autrement que actif interdite";
				logger.error(msg);
				throw new NotAuthorizedException(ExceptionListEnum.NOTAUTHORIZED2, msg);
			}
			
			skypeProfile.setExpirationDateWhenReCreated();
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				if (logger.isErrorEnabled()){
					String msg = "profil non mis à jour : "+skypeProfile.getSIP();
					logger.error(msg);
				}
				return false;
			} else {
				SkypeProfileEvent event = new SkypeProfileEvent(comment + 
						" - nouveau SIP : " + skypeProfile.getSIP(), skypeProfile,
						cilRequester, TypeEventEnum.CREATION);
				repositorySkypeProfileEvent.create(event);
				return true;
			}
		}

		// 2) Cas de la désactivation du profil => mise de la date d'expiration à la date du jour		
		if ((skypeProfile.getStatusProfile() == StatusSkypeProfileEnum.DISABLED)
				&& (profilExisting.getStatusProfile() == StatusSkypeProfileEnum.ENABLED)) {
			try {
				//Verifier que les autres champs ne sont pas modifiée
				if (verifyDifference(profilExisting, skypeProfile)) {
					String msg = "Modification d'autre champs interdite en cas de désactivation, SIP : "+skypeProfile.getSIP();
					logger.error(msg);
					throw new NotAuthorizedException(ExceptionListEnum.NOTAUTHORIZED3, msg);
				}
			} catch (IllegalAccessException e) {
				String msg = "Il est impossible d'accéder au contenu de la classe SkypeProfil";
				logger.error(msg);
				return false;
			}					
				
			skypeProfile.setExpirationDate(new Date());
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				return false;
			} else {
				SkypeProfileEvent event = new SkypeProfileEvent(comment, skypeProfile,
						cilRequester, TypeEventEnum.DESACTIVATION);
				repositorySkypeProfileEvent.create(event);
				return true;
				}		
		}

		// 3) Cas de la réactivation du profil => recalcul de la d'expiration + 2 ans		
		if ((skypeProfile.getStatusProfile() == StatusSkypeProfileEnum.ENABLED)
				&& (profilExisting.getStatusProfile() == StatusSkypeProfileEnum.DISABLED)) {
						
			try {
				//Verifier que les autres champs ne sont pas modifiée
				if (verifyDifference(profilExisting, skypeProfile)) {
					String msg = "Modification d'autre champs interdite en cas de réactivation, SIP : "+skypeProfile.getSIP();
					logger.error(msg);
					throw new NotAuthorizedException(ExceptionListEnum.NOTAUTHORIZED4, msg);
				}
			} catch (IllegalAccessException e) {
				String msg = "Il est impossible d'accéder au contenu de la classe SkypeProfil";
				logger.error(msg);
				return false;
			}		

			skypeProfile.setExpirationDateWhenReCreated();
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				return false;
			} else {
				SkypeProfileEvent event = new SkypeProfileEvent(comment, skypeProfile,
					cilRequester, TypeEventEnum.ACTIVATION);
				repositorySkypeProfileEvent.create(event);
				return true;
			}			
		}
		
		// - Champs modifiés : 
		// 4) Autres cas de mises à jour : on trace les champs qui ont été modifiés

		// on récupère la date d'expiration existante car elle est non modifiable de l'ext.
		skypeProfile.setExpirationDate(profilExisting.getExpirationDate());
		isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
		if (!isUpdatedProfil) {
			return false;
		} else {
			try {
				changedFields = SkypeProfile.difference(profilExisting, skypeProfile);
			} catch (IllegalAccessException e) {
				String msg = "Il est impossible d'accéder au contenu de la classe SkypeProfil";
				logger.error(msg);
				return false;
			}

			for (int i = 0; i < changedFields.size(); i++) {
				if (i==0) comment += " - Champs modifiés : ";
				comment += changedFields.get(i);
				if (i < changedFields.size()-1) comment += ", ";
			}

			SkypeProfileEvent event = new SkypeProfileEvent(comment, skypeProfile,
					cilRequester, TypeEventEnum.MODIFICATION);
			repositorySkypeProfileEvent.create(event);
			return true;
		}
	}


	private boolean verifyDifference(SkypeProfile profilExisting, SkypeProfile skypeProfile) throws IllegalAccessException {
				
 	     for (Field field : profilExisting.getClass().getDeclaredFields()) {
			// You might want to set modifier to public first (if it is not public yet)
			field.setAccessible(true);
			Object value1 = field.get(profilExisting);
			Object value2 = field.get(skypeProfile);
			if ( (value1 != null && value2 != null)
					&& !("collaborater".equals(field.getName()))
					&& !("statusProfile".equals(field.getName()))
					&& !("expirationDate".equals(field.getName()))
					&& !Objects.equals(value1, value2)) {

				   return true;
			}
	    }	    	    
		return false;
	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}

	@Override
	public List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria, boolean sortAscending) {
		
		return repositorySkypeProfile.findAllSkypeProfilePage(numberPage,sizePage,criteria,sortAscending);
	}

	@Override
	public Long countSkypeProfile() {
		
		return repositorySkypeProfile.countSkypeProfile();
	}



}
