package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;

/**
 * Services dédiées au profil skype
 * @author Judicaël.
 *
 */
@Service
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement {

	
	@Autowired
	private ISkypeProfileDomain repositorySkypeProfile;
	
	@Autowired 
	ICollaboraterDomain repositoryCollaborater;
	
	//LEs deux variables ci-dessous sont à supprimer
	//Pour le test uniquement
	
	//private Collaborater collaborater =new Collaborater("479680","mm","mm","mm","mm","mm");
	//private String SIP="mehdi.elouarak@live.skype.com" ;
	

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
	public List<SkypeProfile> findAllSkypeProfile() {
		
		return repositorySkypeProfile.findAllSkypeProfile();
	}

	@Override
	public void deleteSkypeProfile(String sip) {
		
		
		SkypeProfile SkypeProfile = repositorySkypeProfile.findSkypeProfileBySip(sip);
		if (SkypeProfile == null) {
			throw new RuntimeException("Profil skype non trouvé , SIP : "+sip);
		}else {
			repositorySkypeProfile.delete(SkypeProfile);
		}
		
	}
}
