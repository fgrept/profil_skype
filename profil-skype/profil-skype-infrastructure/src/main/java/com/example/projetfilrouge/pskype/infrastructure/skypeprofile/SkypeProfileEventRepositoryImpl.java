package com.example.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.ArrayList;
import java.util.List;

import com.example.projetfilrouge.pskype.domain.skypeprofile.ISkypeProfileEventDomain;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfile;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfileEvent;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaExceptionListEnum;
import com.example.projetfilrouge.pskype.infrastructure.exception.JpaTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

	@Autowired
	private SkypeProfileEventEntityMapper entityMapper;
	
	
	@Autowired
	private ISkypeProfileEventRepository skypeProfileEventRepository;
	
	@Autowired
	private IItCorrespondantRepository itCorrespondantRepository;
	
	@Override
	public void create(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);
		try {
			skypeProfileEventRepository.save(entity);
		}
		catch (Exception e){
			String msg = "Pb JPA lors de la création d'un événement";
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,msg);
		}

	}

	@Override
	public void delete(SkypeProfileEvent skypeProfileEvent) {
		
		SkypeProfileEventEntity entity = entityMapper.mapToEntity(skypeProfileEvent);
		try {
			skypeProfileEventRepository.delete(entity);
		}
		catch (Exception e ){
			String msg = "Pb JPA lors de la suppression d'un événement";
			throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,msg);
		}

	}

	@Override
	public void deleteAllEventByProfile(SkypeProfile skypeProfile) {
		List<SkypeProfileEventEntity> listEventProfile;
		try {
			listEventProfile = skypeProfileEventRepository.findBySkypeProfileSIP(skypeProfile.getSIP());
		}catch (Exception e){
			String msg  = "Pb JPA pour récupérer les événements d'un profil skype";
			throw new JpaTechnicalException(JpaExceptionListEnum.READ_ACCESS,msg);
		}

		for (SkypeProfileEventEntity entity : listEventProfile) {
			try {
				skypeProfileEventRepository.delete(entity);
			}catch (Exception e){
				String msg = "Pb JPA lors de la suppression d'un événement";
				throw new JpaTechnicalException(JpaExceptionListEnum.WRITE_ACCESS,msg);
			}

		}
	}

	@Override
	public List<SkypeProfileEvent> findAllEventBySkypeProfile(SkypeProfile skypeProfile) {
		
		List<SkypeProfileEventEntity> Entitys;
		try {
			Entitys = skypeProfileEventRepository.findBySkypeProfileSIP(skypeProfile.getSIP());
		}catch (Exception e){
			String msg  = "Pb JPA pour récupérer les événements d'un profil skype";
			throw new JpaTechnicalException(JpaExceptionListEnum.READ_ACCESS,msg);
		}
		return entityMapper.mapToDomainList(Entitys);	
		
	}

	@Override
	public List<SkypeProfileEvent> findAllEventByItCorrespondantId(String collaboraterId) {
		
		List<SkypeProfileEventEntity> Entitys;
		try {
			Entitys = skypeProfileEventRepository.findByItCorrespondantItCorrespondantId(collaboraterId);
		}catch (Exception e){
			String msg  = "Pb JPA pour récupérer les événements d'un utilisateur";
			throw new JpaTechnicalException(JpaExceptionListEnum.READ_ACCESS,msg);
		}

		if (Entitys==null) {
			return new ArrayList<>();
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
