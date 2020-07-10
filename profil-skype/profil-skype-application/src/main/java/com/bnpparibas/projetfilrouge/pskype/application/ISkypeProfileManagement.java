package com.bnpparibas.projetfilrouge.pskype.application;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;

public interface ISkypeProfileManagement {

	
	void addNewSkypeProfile(SkypeProfile SkypeProfile);

	SkypeProfile consultActiveSkypeProfile(String sip);
	
}
