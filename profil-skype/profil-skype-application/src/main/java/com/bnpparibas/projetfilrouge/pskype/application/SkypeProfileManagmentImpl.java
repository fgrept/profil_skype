package com.bnpparibas.projetfilrouge.pskype.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.IRepositorySkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;

@Service
public class SkypeProfileManagmentImpl implements ISkypeProfileManagement {

	
	@Autowired
	private IRepositorySkypeProfile repositorySkypeProfile;
	
	//LEs deux variables ci-dessous sont à supprimer
	//Pour le test uniquement
	
	private Collaborater collaborater =new Collaborater("479680","mm","mm","mm","mm","mm");
	private String SIP="mehdi.elouarak@live.skype.com" ;
	
	@Override
	public void addNewSkypeProfile(SkypeProfile SkypeProfile) {
		
		//Instanciation du SkypeProfile
		final SkypeProfile skypeprofile = new SkypeProfile(SIP, collaborater) ;
		
		//Ajout du SkypeProfile
		
		repositorySkypeProfile.addSkypeProfile(skypeprofile);
		
	}

	@Override
	public SkypeProfile consultActiveSkypeProfile(String sip) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
