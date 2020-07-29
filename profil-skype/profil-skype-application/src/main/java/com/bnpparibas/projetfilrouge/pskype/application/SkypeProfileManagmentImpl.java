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
import com.bnpparibas.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.exception.NotFoundException;

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
	public List<SkypeProfile> findSkypeProfileWithCriteriaPage(SkypeProfile profilDom, int numberPage, int sizePage,
			String sortCriteria, boolean sortAscending) {
		// TODO Auto-generated method stub
		return repositorySkypeProfile.findAllSkypeProfileFiltersPage(profilDom,numberPage,sizePage,sortCriteria,sortAscending);
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
		
		// 1) Cas de la modification d'adresse SIP => création d'un nouveau profil
		if (!(skypeProfile.getSIP().equals(profilExisting.getSIP()))) {
			skypeProfile.setExpirationDateWhenReCreated();
			isUpdatedProfil = repositorySkypeProfile.update(skypeProfile);
			if (!isUpdatedProfil) {
				logger.error("profil non mis à jour : "+skypeProfile.getSIP());
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
		
		return true;
	}


	@Override
	public SkypeProfile findSkypeProfilFromCollab(String idAnnuaire) {
		
		return repositorySkypeProfile.findSkypeProfileByIdCollab(idAnnuaire);
	}

	@Override
	public List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria, boolean sortAscending) {
		// TODO Auto-generated method stub
		return repositorySkypeProfile.findAllSkypeProfilePage(numberPage,sizePage,criteria,sortAscending);
	}

	@Override
	public Long countSkypeProfile() {
		
		return repositorySkypeProfile.countSkypeProfile();
	}



}
