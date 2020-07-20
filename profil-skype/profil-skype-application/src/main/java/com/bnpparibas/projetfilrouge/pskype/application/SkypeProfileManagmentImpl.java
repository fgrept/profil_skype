package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;

/**
 * Services dédiées au profil skype
 * @author Judicaël.
 *
 */
@Service
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement,ISkypeProfileEventManagement  {

	
	@Autowired
	private ISkypeProfileDomain repositorySkypeProfile;
	
	@Autowired 
	ICollaboraterDomain repositoryCollaborater;
	
	@Autowired
	private ISkypeProfileEventDomain repositorySkypeProfileEvent;
	
	@Autowired 
	IItCorrespondantDomain repositoryItCorrespondant;

	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {
		// TODO Auto-generated method stub
		
		return repositorySkypeProfile.consultSkypeProfile(sip, StatusSkypeProfileEnum.ENABLED);
	}

	
	@Override
	public void addNewSkypeProfile(SkypeProfileDto skypeProfileDto) {
		
		SkypeProfile skypeProfile = new SkypeProfile();
		skypeProfile.setSIP(skypeProfileDto.getSIP());
		skypeProfile.setCollaborater(repositoryCollaborater.findByCollaboraterId(skypeProfileDto.getCollaboraterId()));
		skypeProfile.setDialPlan(skypeProfileDto.getDialPlan());
		skypeProfile.setEnterpriseVoiceEnabled(skypeProfileDto.isEnterpriseVoiceEnabled());
		skypeProfile.setExchUser(skypeProfileDto.getExchUser());
		skypeProfile.setExpirationDate(skypeProfileDto.getExpirationDate());
		skypeProfile.setExUmEnabled(skypeProfileDto.isExUmEnabled());
		skypeProfile.setObjectClass(skypeProfileDto.getObjectClass());
		skypeProfile.setStatusProfile(skypeProfileDto.getStatusProfile());
		repositorySkypeProfile.create(skypeProfile);
	}
	
	
	@Override
	public void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfileDto,SkypeProfileEventDto skypeProfileEventDto ) {
		

		SkypeProfile skypeProfile = new SkypeProfile();
		
		skypeProfile.setSIP(skypeProfileDto.getSIP());
		skypeProfile.setCollaborater(repositoryCollaborater.findByCollaboraterId(skypeProfileDto.getCollaboraterId()));
		skypeProfile.setDialPlan(skypeProfileDto.getDialPlan());
		skypeProfile.setEnterpriseVoiceEnabled(skypeProfileDto.isEnterpriseVoiceEnabled());
		skypeProfile.setExchUser(skypeProfileDto.getExchUser());
		skypeProfile.setExpirationDate(skypeProfileDto.getExpirationDate());
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

	@Override
	public void deleteSkypeProfile(String sip) {
		
		
		
		SkypeProfile skypeProfile = repositorySkypeProfile.findSkypeProfileBySip(sip);
		
		if (skypeProfile == null) {
			throw new RuntimeException("Profil skype non trouvé , SIP : "+sip);
		}else {
			repositorySkypeProfile.delete(skypeProfile);
	
		}
		
	}

	@Override
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





}
