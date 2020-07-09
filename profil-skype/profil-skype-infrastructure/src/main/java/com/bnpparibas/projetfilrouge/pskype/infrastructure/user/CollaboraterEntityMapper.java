package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;

@Component
public class CollaboraterEntityMapper extends AbstractMapper<Collaborater, CollaboraterEntity>{

	@Override
	public Collaborater mapToDomain(CollaboraterEntity entity) {
		
		Collaborater collab = new Collaborater();
		collab.setDeskPhoneNumber(entity.getDeskPhoneNumber());
		collab.setFirstNamePerson(entity.getFirstName());
		collab.setLastNamePerson(entity.getLastName());
		collab.setMailAdress(entity.getMailAdress());
		collab.setMobilePhoneNumber(entity.getMobilePhoneNumber());
		
		return collab;
	}

	@Override
	public CollaboraterEntity mapToEntity(Collaborater dto) {
			
		return null;
	}
	
	

}
