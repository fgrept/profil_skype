package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.AbstractMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntityMapper;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntityMapper;
/**
 * 
 * Mapper entre entité et domaine pour la classe SkypeProfileEvent
 * @author Judicaël
 *
 */
@Component
public class SkypeProfileEventEntityMapper extends AbstractMapper<SkypeProfileEvent, SkypeProfileEventEntity> {

	@Autowired
	ItCorrespondantEntityMapper itCorrespondantMapper;
	
	@Autowired
	SkypeProfileEntityMapper skypeProfileMapper;
	
	@Override
	public SkypeProfileEvent mapToDomain(SkypeProfileEventEntity entity) {
		
		SkypeProfileEvent skypeProfileEvent = new SkypeProfileEvent();
		skypeProfileEvent.setDateEvent(entity.getDateEvent());
		skypeProfileEvent.setCommentEvent(entity.getCommentEvent());
		skypeProfileEvent.setTypeEvent(entity.getTypeEvent());
		skypeProfileEvent.setItCorrespondant(itCorrespondantMapper.mapToDomain(entity.getItCorrespondant()));
		skypeProfileEvent.setSkypeProfile(skypeProfileMapper.mapToDomain(entity.getSkypeProfile()));
		
		return skypeProfileEvent;
	}

	@Override
	public SkypeProfileEventEntity mapToEntity(SkypeProfileEvent dto) {
		
		SkypeProfileEventEntity skypeProfileEventEntity = new SkypeProfileEventEntity();
		skypeProfileEventEntity.setDateEvent(dto.getDateEvent());
		skypeProfileEventEntity.setCommentEvent(dto.getCommentEvent());
		skypeProfileEventEntity.setSkypeProfile(skypeProfileMapper.mapToEntity(dto.getSkypeProfile()));
		skypeProfileEventEntity.setItCorrespondant(itCorrespondantMapper.mapToEntity(dto.getItCorrespondant()));
		skypeProfileEventEntity.setTypeEvent(dto.getTypeEvent());
		
		return skypeProfileEventEntity;
	}

}
