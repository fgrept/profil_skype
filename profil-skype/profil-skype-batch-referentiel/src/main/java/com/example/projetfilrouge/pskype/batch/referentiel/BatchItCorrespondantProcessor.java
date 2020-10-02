package com.example.projetfilrouge.pskype.batch.referentiel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.ICollaboraterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.projetfilrouge.pskype.infrastructure.user.IItCorrespondantRepository;
import com.example.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntity;
import com.example.projetfilrouge.pskype.batch.referentiel.dto.ItCorrespondantDto;

public class BatchItCorrespondantProcessor implements ItemProcessor<ItCorrespondantDto, ItCorrespondantEntity> {
	
	Logger log = LoggerFactory.getLogger(BatchItCorrespondantProcessor.class);
	private int cptLigne = 1;
	
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
		
		cptLigne +=1;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
		
	    Set<ConstraintViolation<ItCorrespondantDto>> constraintViolations = 
	    	      validator.validate(item);
	    if (constraintViolations.size() > 0 ) {
	    	String sError = "Données de l'it correspondant incorrects en ligne : " + cptLigne;
	    	log.error(sError);
	    	return null;
	      }
	    
		CollaboraterEntity collaboraterEntity = collaboraterRepository.findByCollaboraterId(item.getIdAnnuaire());
		if (collaboraterEntity == null) {
			return null;
		}
		ItCorrespondantEntity entity = itcorrespondantRepository.findByCollaboraterCollaboraterId(item.getIdAnnuaire());
		if (entity==null) {
			
			entity = new ItCorrespondantEntity(collaboraterEntity, item.getIdAnnuaire(),passwordEncoder().encode(item.getPassword()));
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
