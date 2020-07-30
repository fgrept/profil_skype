package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface ISkypeProfileEventDomain {
	
	void create(SkypeProfileEvent skypeProfileEvent);
	void delete(SkypeProfileEvent skypeProfileEvent);
	void deleteAllEventByProfile(SkypeProfile skypeProfile);
	List<SkypeProfileEvent> findAllEventBySkypeProfile(SkypeProfile skypeProfile);
	List<SkypeProfileEvent> findAllEventByItCorrespondantId(String collaboraterId);
	boolean updateEventItCorrespondant(ItCorrespondant itCorrespondant, ItCorrespondant itCorrespondantNew);
} 
