package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.ArrayList;
import java.util.Date;
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
	private ISkypeProfileEventDomain repositorySkypeProfileEvent;
	
	@Autowired 
	private IItCorrespondantDomain repositoryItCorrespondant;
	
	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {
		// TODO Auto-generated method stub
		
		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	
	@Override
	public boolean addNewSkypeProfile(SkypeProfile skypeProfile, String idAnnuaireCIL, String eventComment) {
		
		 if (repositorySkypeProfile.create(skypeProfile) == true) {
			 ItCorrespondant itCorrespondant = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);
			 
			 // création de l'évènement associé
			 if (itCorrespondant != null) {
				 SkypeProfileEvent event = new SkypeProfileEvent(eventComment, skypeProfile, itCorrespondant, TypeEventEnum.CREATION);
				 repositorySkypeProfileEvent.create(event);
				 return true;
			}
		} 
		 return false;
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
			return false;
		}
		
		// Récupération du CIL demandant la modif
		ItCorrespondant cilRequester = repositoryItCorrespondant.findItCorrespondantByCollaboraterId(idAnnuaireCIL);		
		if (cilRequester == null) {
			return false;
		}
		
		// 1) Cas de la modification d'adresse SIP => création d'un nouveau profil
		if (skypeProfile.getSIP() != profilExisting.getSIP()) {
			skypeProfile.setExpirationDateWhenReCreated();
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				return false;
			} else {
				SkypeProfileEvent event = new SkypeProfileEvent(comment + 
						"" + skypeProfile.getSIP(), skypeProfile,
						cilRequester, TypeEventEnum.CREATION);
				repositorySkypeProfileEvent.create(event);
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
				for (int i = 0; i <= changedFields.size(); i++) {
					if (i==0) comment += " - Champs modifiés : ";
					comment += changedFields.get(i);
					if (i == changedFields.size()) comment += ", ";
				}
			
				SkypeProfileEvent event = new SkypeProfileEvent(comment, skypeProfile,
						cilRequester, TypeEventEnum.MODIFICATION);
				repositorySkypeProfileEvent.create(event);
			}			
		}
		
		return true;
	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}

}
