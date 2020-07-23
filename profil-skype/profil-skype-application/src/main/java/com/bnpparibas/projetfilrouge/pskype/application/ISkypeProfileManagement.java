package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;


public interface ISkypeProfileManagement {

	

	SkypeProfile consultActiveSkypeProfile(String sip);

	
	void addNewSkypeProfile(SkypeProfile skypeProfile, String itcorrespondantId);
	
	
	void addNewSkypeProfileWithEvent(SkypeProfileDto skypeProfile, SkypeProfileEventDto skypeProfileEventDto);
	
	
	List<SkypeProfile> findAllSkypeProfile();
	
	
	void deleteSkypeProfile(String sip);
	


	void updateSkypeProfile(String sip, SkypeProfile skypeProfile, String itCorrespondantId);

	
	
}
