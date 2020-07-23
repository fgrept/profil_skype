package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileEventDto;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;

public interface ISkypeProfileEventManagement {
	
	

	void addNewSkypeProfileEvent(SkypeProfileEventDto skypeProfileEvent,SkypeProfile skypeProfile);
	void addNewSkypeProfileEventForCreation(SkypeProfile skypeProfile, String itCollaboraterId);	
	void addNewSkypeProfileEventForUpdate(SkypeProfile skypeProfile, SkypeProfile skypeProfileDB,String itCorrespondantId);

}
