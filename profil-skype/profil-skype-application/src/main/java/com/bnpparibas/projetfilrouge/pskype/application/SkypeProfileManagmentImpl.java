package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;

/**
 * Services dédiées au profil skype
 * 
 * @author 479680.
 *
 */
@Service
@Transactional
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement, ISkypeProfileEventManagement {

	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantManagementImpl.class);
	
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
		// TODO Auto-generated method stub

		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	@Override
	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment) {
		
		
		ItCorrespondant itCorrespondant = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
			 
		if (itCorrespondant != null) {
			// création de l'évènement associé
			if (repositorySkypeProfile.create(skypeProfile) == true) {
				 SkypeProfileEvent event = new SkypeProfileEvent(eventComment, skypeProfile, itCorrespondant, TypeEventEnum.CREATION);
				 repositorySkypeProfileEvent.create(event);
				 return true;
			}else {
				logger.error("Profil skype non créé :"+skypeProfile.getSIP());
				return false;
			}
		}else {
			logger.error("Pas d'it correspondant trouvé pour id annuaire :"+idAnnuaireCIL);
			return false;
		}
	}
	

/* Méthode conservée si détection dynamique des champs ne fonctionne pas
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

	}*/

	
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

	@Override
	public boolean updateSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment) {
		
		boolean isUpdatedProfil = false;
		List<String> changedFields = new ArrayList<String>();
		String comment = "Commentaire utilisateur : <<" + eventComment + ">>";
		
		// Lors de la mise à jour d'un profil : tous les champs peuvent être modifiés
		// On récupère donc le profil existant associé au collaborateur
		SkypeProfile profilExisting = findSkypeProfilFromCollab(skypeProfile.getCollaborater().getCollaboraterId());		
		if (profilExisting == null) {
			logger.error("mise à jour sur profil inexistant");
			return false;
		}
		
		// Récupération du CIL demandant la modif
		ItCorrespondant cilRequester = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);		
		if (cilRequester == null) {
			logger.error("cil non trouvé, id annuaire: "+idAnnuaireCIL);
			return false;
		}
		
		// 1) Cas de la modification d'adresse SIP => création d'un nouveau profil
		if (!(skypeProfile.getSIP().equals(profilExisting.getSIP()))) {
			skypeProfile.setExpirationDateWhenReCreated();
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				logger.error("profil non mis à jour : "+skypeProfile.getSIP());
				return false;
			} else {
				SkypeProfileEvent event = new SkypeProfileEvent(comment + 
						"" + skypeProfile.getSIP(), skypeProfile,
						cilRequester, TypeEventEnum.CREATION);
				repositorySkypeProfileEvent.create(event);
				return true;
			}
		}

		// 2) Cas de la désactivation du profil => mise de la date d'expiration à la date du jour		
		if ((skypeProfile.getStatusProfile() == StatusSkypeProfileEnum.DISABLED)
				&& (profilExisting.getStatusProfile() == StatusSkypeProfileEnum.ENABLED)) {
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
		if (!isUpdatedProfil) {
			// on récupère la date d'expiration existante car elle est non modifiable de l'ext.
			skypeProfile.setExpirationDate(profilExisting.getExpirationDate());
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				return false;
			} else {
				try {
					changedFields = SkypeProfile.difference(profilExisting, skypeProfile);				
				} catch (IllegalAccessException e) {
					// TODO Mettre une log erreur
					e.printStackTrace();
					return false;
				}
				if (changedFields.size() > 0) {
					comment += " - Champs modifiés : ";
				}
				for (String attribut:changedFields) {
					comment += attribut+" - ";
				}
			
				SkypeProfileEvent event = new SkypeProfileEvent(comment, skypeProfile,
						cilRequester, TypeEventEnum.MODIFICATION);
				repositorySkypeProfileEvent.create(event);
				return true;
			}			
		}
		
		return true;
	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}


}
