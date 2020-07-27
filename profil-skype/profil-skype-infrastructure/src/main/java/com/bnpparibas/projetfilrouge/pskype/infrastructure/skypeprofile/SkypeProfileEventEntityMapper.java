package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntityMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.IItCorrespondantRepository;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntityMapper;
/**
 * 
 * Mapper entre entité et domaine pour la classe SkypeProfileEvent
 * @author Judicaël
 *
 */
@Component
public class SkypeProfileEventEntityMapper extends AbstractMapper<SkypeProfileEvent, SkypeProfileEventEntity> {

//Utilisation des repository ItCoorespondant et skype Profil au lieu des Mappers ==> A REVOIR
//	@Autowired
//	ItCorrespondantEntityMapper itCorrespondantMapper;
	
//	@Autowired
//	SkypeProfileEntityMapper skypeProfileMapper;
	
	 
	@Autowired
	private ISkypeProfileDomain repositorySkypeProfileDomain;
	
	@Autowired 
	IItCorrespondantDomain repositoryItCorrespondantDomain;
	
	@Autowired
	private ISkypeProfileRepository repositorySkypeProfile;
	
	@Autowired 
	IItCorrespondantRepository repositoryItCorrespondant;
	
	@Override
	public SkypeProfileEvent mapToDomain(SkypeProfileEventEntity entity) {
		
		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
		skypeProfileEvent.setDateEvent(entity.getDateEvent());
		skypeProfileEvent.setCommentEvent(entity.getCommentEvent());
		skypeProfileEvent.setTypeEvent(entity.getTypeEvent());
		
	//	skypeProfileEvent.setItCorrespondant(itCorrespondantMapper.mapToDomain(entity.getItCorrespondant()));
	//	skypeProfileEvent.setSkypeProfile(skypeProfileMapper.mapToDomain(entity.getSkypeProfile()));
		
		skypeProfileEvent.setItCorrespondant(repositoryItCorrespondantDomain.findItCorrespondantByCollaboraterId(skypeProfileEvent.getItCorrespondant().getCollaboraterId()));
		
		skypeProfileEvent.setSkypeProfile(repositorySkypeProfileDomain.findSkypeProfileBySip(skypeProfileEvent.getSkypeProfile().getSIP()));
				
		return skypeProfileEvent;
	}

	@Override
	public SkypeProfileEventEntity mapToEntity(SkypeProfileEvent dto) {
		
		SkypeProfileEventEntity skypeProfileEventEntity = new SkypeProfileEventEntity();
		skypeProfileEventEntity.setDateEvent(dto.getDateEvent());
		skypeProfileEventEntity.setCommentEvent(dto.getCommentEvent());
		
		
		//skypeProfileEventEntity.setSkypeProfile(skypeProfileMapper.mapToEntity(dto.getSkypeProfile()));
		//skypeProfileEventEntity.setItCorrespondant(itCorrespondantMapper.mapToEntity(dto.getItCorrespondant()));

		skypeProfileEventEntity.setSkypeProfile(repositorySkypeProfile.findBySIP(dto.getSkypeProfile().getSIP()));
		skypeProfileEventEntity.setItCorrespondant(repositoryItCorrespondant.findByCollaboraterCollaboraterId(dto.getItCorrespondant().getCollaboraterId())) ;
		
		skypeProfileEventEntity.setTypeEvent(dto.getTypeEvent());
		
		return skypeProfileEventEntity;
	}

}
