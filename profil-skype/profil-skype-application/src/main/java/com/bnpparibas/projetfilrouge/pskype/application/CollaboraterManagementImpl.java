package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
/**
 * Service exposant les méthodes d'interfaction avec le Collaborateur
 * @author Judicaël
 *
 */
@Service
public class CollaboraterManagementImpl implements ICollaboraterManagment {

@Autowired
private ICollaboraterDomain collaboraterDomain;
	
	@Override
	public void createCollaborater(String nom, String prenom, String id, String deskPhoneNumber,
			String mobilePhoneNumber, String mailAdress) {
		Collaborater collaborater = new Collaborater(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress);
		collaboraterDomain.create(collaborater);
	}

	@Override
	public List<Collaborater> listCollaborater() {
		// TODO Auto-generated method stub
		return null;
	}

}
