package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;

@Repository
public class SkypeProfileEventRepositoryImpl implements ISkypeProfileEventDomain {

	@Autowired
	private SkypeProfileEventEntityMapper entityMapper;
	
	@Autowired
	private SkypeProfileEntityMapper entityMapperSkypeProfile;
	
	@Autowired
	private ISkypeProfileEventRepository skypeProfileEventRepository;
	
	@Override
	public void create(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);
		skypeProfileEventRepository.save(entity);
	}

	@Override
	public void update(SkypeProfileEvent skypeProfileEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);
		skypeProfileEventRepository.delete(entity);
	}

	@Override
	public void deleteAllEventByProfile(SkypeProfile skypeProfile) {
		
		List<SkypeProfileEventEntity> listEventProfile = skypeProfileEventRepository.findBySkypeProfile(entityMapperSkypeProfile.mapToEntity(skypeProfile));
		for (SkypeProfileEventEntity entity : listEventProfile) {
			skypeProfileEventRepository.delete(entity);
		}
	}

	@Override
	public List<SkypeProfileEvent> findAllEventBySkypeProfile(SkypeProfile skypeProfile) {
		
		List<SkypeProfileEvent> listEventProfile = new ArrayList<SkypeProfileEvent>();
		for (SkypeProfileEventEntity entity : skypeProfileEventRepository.findBySkypeProfile(entityMapperSkypeProfile.mapToEntity(skypeProfile))){
			listEventProfile.add(entityMapper.mapToDomain(entity));
		}
		
		
		return null;
	}

}
