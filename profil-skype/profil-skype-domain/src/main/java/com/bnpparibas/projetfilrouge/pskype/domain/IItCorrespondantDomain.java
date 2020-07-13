package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;
import java.util.Set;

public interface IItCorrespondantDomain {
	void create(ItCorrespondant itCorrespondant);
	void update(String idAnnuaire, Set<RoleTypeEnum> roles);
	void delete(ItCorrespondant itCorrespondant);
	List<ItCorrespondant> findAllItCorrespondant();
	List<ItCorrespondant> findAllItCorrespondantFilters(String id, String lastName, String firstName, String deskPhone, String mobilePhone, String mailAddress);
	ItCorrespondant findItCorrespondantByCollaboraterId(String id);
}
