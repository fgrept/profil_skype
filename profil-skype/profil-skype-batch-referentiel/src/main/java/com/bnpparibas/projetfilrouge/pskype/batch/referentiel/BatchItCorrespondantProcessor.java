package com.bnpparibas.projetfilrouge.pskype.batch.referentiel;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.ItCorrespondantDto;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ICollaboraterRepository;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.IItCorrespondantRepository;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntity;

public class BatchItCorrespondantProcessor implements ItemProcessor<ItCorrespondantDto, ItCorrespondantEntity> {

	@Autowired
	private ICollaboraterRepository collaboraterRepository;
	
	@Autowired
	private IItCorrespondantRepository itcorrespondantRepository;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(); // or any other password encoder
	}
	
	@Override
	public ItCorrespondantEntity process(ItCorrespondantDto item) throws Exception {
		
		if (item.getIdAnnuaire()==null) {
			return null;
		}
		CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(item.getIdAnnuaire());
		if (collaboraterEntity == null) {
			return null;
		}
		ItCorrespondantEntity entity = itcorrespondantRepository.findByCollaboraterId(item.getIdAnnuaire());
		if (entity==null) {
			
			entity = new ItCorrespondantEntity(collaboraterEntity, passwordEncoder().encode(item.getPassword()));
		}
		switch (item.getRole())
		{
			case "ROLE_ADMIN":
				entity.addRoles(RoleTypeEnum.ROLE_ADMIN);
				entity.addRoles(RoleTypeEnum.ROLE_USER);
				entity.addRoles(RoleTypeEnum.ROLE_RESP);
				break;
			case "ROLE_RESP":
				entity.addRoles(RoleTypeEnum.ROLE_RESP);
				entity.addRoles(RoleTypeEnum.ROLE_USER);
				break;
			case "ROLE_USER":
				entity.addRoles(RoleTypeEnum.ROLE_USER);
				break;
			default:
				return null;
		}
		return entity;
	}

}
