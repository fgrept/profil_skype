package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.example.projetfilrouge.pskype.infrastructure.AbstractMapper;
import com.example.projetfilrouge.pskype.infrastructure.user.IItCorrespondantRepository;
import com.example.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntityMapper;
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
	
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileEventEntityMapper.class);
	
//	@Autowired
//	private ISkypeProfileDomain repositorySkypeProfileDomain;
	
//	@Autowired 
//	IItCorrespondantDomain repositoryItCorrespondantDomain;
	
	@Autowired
	private ISkypeProfileRepository repositorySkypeProfile;
	
	@Autowired 
	private IItCorrespondantRepository repositoryItCorrespondant;
	
	@Autowired
	private SkypeProfileEntityMapper mapperSkypeProfile;
	
	@Autowired
	private ItCorrespondantEntityMapper mapperItCorrespondant;
	
	@Override
	public SkypeProfileEvent mapToDomain(SkypeProfileEventEntity entity) {
		
		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
		skypeProfileEvent.setDateEvent(entity.getDateEvent());
		skypeProfileEvent.setCommentEvent(entity.getCommentEvent());
		skypeProfileEvent.setTypeEvent(entity.getTypeEvent());
		
	//	skypeProfileEvent.setItCorrespondant(itCorrespondantMapper.mapToDomain(entity.getItCorrespondant()));
	//	skypeProfileEvent.setSkypeProfile(skypeProfileMapper.mapToDomain(entity.getSkypeProfile()));
		
		
		if (entity.getItCorrespondant()==null ||entity.getItCorrespondant().getCollaborater()==null) {
			logger.error("Aucun it correspondant trouvé");
		}else {
			skypeProfileEvent.setItCorrespondant(mapperItCorrespondant.mapToDomain(repositoryItCorrespondant.findByCollaboraterCollaboraterId(entity.getItCorrespondant().getCollaborater().getCollaboraterId())));
		}
		skypeProfileEvent.setSkypeProfile(mapperSkypeProfile.mapToDomain(repositorySkypeProfile.findBySIP(entity.getSkypeProfile().getSIP())));
				
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
