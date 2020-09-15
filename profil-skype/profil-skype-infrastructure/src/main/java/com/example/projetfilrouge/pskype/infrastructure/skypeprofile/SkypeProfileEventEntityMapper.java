package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfileEvent;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


	private static Logger logger = LoggerFactory.getLogger(SkypeProfileEventEntityMapper.class);

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

		ItCorrespondant itCorrespondant = null;

		if (entity.getItCorrespondant()==null ||entity.getItCorrespondant().getCollaborater()==null) {
			logger.error("Aucun it correspondant trouvé");
		}else {
			itCorrespondant = mapperItCorrespondant.mapToDomain(repositoryItCorrespondant.findByCollaboraterCollaboraterId(entity.getItCorrespondant().getCollaborater().getCollaboraterId()));
		}
		return new SkypeProfileEvent(entity.getDateEvent(),
				entity.getCommentEvent(),
				mapperSkypeProfile.mapToDomain(repositorySkypeProfile.findBySIP(entity.getSkypeProfile().getSIP())),
				itCorrespondant,
				entity.getTypeEvent());

	}

	@Override
	public SkypeProfileEventEntity mapToEntity(SkypeProfileEvent dto) {

		return new SkypeProfileEventEntity(dto.getTypeEvent(),dto.getCommentEvent(),
					repositorySkypeProfile.findBySIP(dto.getSkypeProfile().getSIP()),
				repositoryItCorrespondant.findByCollaboraterCollaboraterId(dto.getItCorrespondant().getCollaboraterId()));
	}

}
