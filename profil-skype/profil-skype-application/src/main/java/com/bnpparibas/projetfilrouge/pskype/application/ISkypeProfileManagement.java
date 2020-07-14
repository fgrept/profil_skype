package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.dto.SkypeProfileDto;

public interface ISkypeProfileManagement {

	
//	void addNewSkypeProfile(SkypeProfile SkypeProfile);

	SkypeProfile consultActiveSkypeProfile(String sip);

	void addNewSkypeProfile(SkypeProfileDto skypeProfile);
	
	List<SkypeProfile> findAllSkypeProfile();
	
	void deleteSkypeProfile(String sip);
}
