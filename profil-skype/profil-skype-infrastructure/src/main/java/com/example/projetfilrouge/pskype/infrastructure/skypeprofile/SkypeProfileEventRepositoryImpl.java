package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.example.projetfilrouge.pskype.domain.ItCorrespondant;
import com.example.projetfilrouge.pskype.domain.SkypeProfile;
import com.example.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.example.projetfilrouge.pskype.infrastructure.user.IItCorrespondantRepository;
import com.example.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntity;
/**
 * Dédiée au profil Skype Elle assure la correspondance entre les méthodes
 * exposées de la couche domaine et celles da la couche infrastructure. 
 * 
 * @author la fabrique
 *
 */
@Repository
public class SkypeProfileEventRepositoryImpl implements ISkypeProfileEventDomain {
	
	private static Logger logger = LoggerFactory.getLogger(SkypeProfileEventRepositoryImpl.class);
	
	@Autowired
	private SkypeProfileEventEntityMapper entityMapper;
	
	
	@Autowired
	private ISkypeProfileEventRepository skypeProfileEventRepository;
	
	@Autowired
	private IItCorrespondantRepository itCorrespondantRepository;
	
	@Override
	public void create(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);		
		skypeProfileEventRepository.save(entity);
	}

	@Override
	public void delete(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);
		skypeProfileEventRepository.delete(entity);
	}

	@Override
	public void deleteAllEventByProfile(SkypeProfile skypeProfile) {
		
		List<SkypeProfileEventEntity> listEventProfile = skypeProfileEventRepository.findBySkypeProfileSIP(skypeProfile.getSIP());
		for (SkypeProfileEventEntity entity : listEventProfile) {
			skypeProfileEventRepository.delete(entity);
		}
	}

	@Override
	public List<SkypeProfileEvent> findAllEventBySkypeProfile(SkypeProfile skypeProfile) {		
		
		List<SkypeProfileEventEntity> Entitys = skypeProfileEventRepository.findBySkypeProfileSIP(skypeProfile.getSIP());		
		for (SkypeProfileEventEntity entity:Entitys) {
			logger.info(entity.toString());
		}
		return entityMapper.mapToDomainList(Entitys);	
		
	}

	@Override
	public List<SkypeProfileEvent> findAllEventByItCorrespondantId(String collaboraterId) {
		
		List<SkypeProfileEventEntity> Entitys = skypeProfileEventRepository.findByItCorrespondantItCorrespondantId(collaboraterId);
		if (Entitys==null) {
			return null;
		}else {
			return entityMapper.mapToDomainList(Entitys);
		}
	}

	@Override
	public boolean updateEventItCorrespondant(ItCorrespondant itCorrespondant, ItCorrespondant itCorrespondantNew) {
		
		ItCorrespondantEntity correspondantEntityNew;
		if (itCorrespondantNew ==null) {
			correspondantEntityNew = null;
		}else {
			correspondantEntityNew = itCorrespondantRepository.findByItCorrespondantId(itCorrespondantNew.getCollaboraterId());
		}
		for (SkypeProfileEventEntity entity : skypeProfileEventRepository.findByItCorrespondantItCorrespondantId(itCorrespondant.getCollaboraterId())) {
			entity.setItCorrespondant(correspondantEntityNew);
			if (skypeProfileEventRepository.save(entity)==null) {
				return false;
			}
		}
		return true;
	}

}
