package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;
import java.util.Set;

import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

/**
 * Exposition des méthodes du service de gestion des CIL
 * @author Judicaël
 *
 */
public interface IItCorrespondantManagment {
	void createCIL(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,
			String mailAdress);
	List<ItCorrespondant> listItCorrespondant();
	List<ItCorrespondant> listItCorrespondantFilters(String id, String lastName, String firstName, String deskPhone, String mobilePhone, String mailAddress);
	void updateRoleCIL(String idAnnuaire, Set<RoleTypeEnum> roles);
	void deleteCIL(String idAnnuaire);
}
