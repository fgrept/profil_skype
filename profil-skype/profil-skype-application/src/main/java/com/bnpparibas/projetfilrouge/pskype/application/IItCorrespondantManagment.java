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

	List<ItCorrespondant> listItCorrespondant();
	List<ItCorrespondant> listItCorrespondantFilters(String id, String lastName, String firstName, String deskPhone, String mobilePhone, String mailAddress);
	void updateRoleCIL(String idAnnuaire, Set<RoleTypeEnum> roles);
	void deleteCIL(String idAnnuaire);
	/**
	 * Cette méthode permet la création d'un CIL avec le rôle user par défaut (US010)
	 * @param String nom
	 * @param String prénom
	 * @param String id annuaire
	 * @param String numéro de téléphone fixe
	 * @param String numéro de téléphone mobile
	 * @param String adresse mail
	 * @author Judicaël
	 * @version V0.1
	 */
	void createCIL(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,
			String mailAdress, String password);
	/**
	 * Mise à jour du password
	 * @param idAnnuaire
	 * @param password
	 */
	void updatePasswordCIL(String idAnnuaire, String oldPassword, String newPassword);
}
