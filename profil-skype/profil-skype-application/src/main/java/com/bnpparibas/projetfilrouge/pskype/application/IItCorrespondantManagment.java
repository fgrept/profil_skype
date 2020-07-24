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
	/**
	 * Recherche multi critères d'un utilisateur.
	 * @param itCorrespondant
	 * @return List d'itCorrespondant
	 */
	List<ItCorrespondant> listItCorrespondantFilters(ItCorrespondant itCorrespondant);
	/**
	 * Mise à jour de liste des rôles d'un utilisateur
	 * @param idAnnuaire
	 * @param roles
	 * @return boolean
	 */
	boolean updateRoleItCorrespondant(String idAnnuaire, Set<RoleTypeEnum> roles);
	/**
	 * Suppression d'un utilisateur à partir de son id annuaire
	 * @param idAnnuaire
	 * @return boolean
	 */
	boolean deleteItCorrespondant(String idAnnuaire);
/**
 *  Cette méthode permet la création d'un utilisateur avec le rôle user par défaut (US010)
 * @param itCorrespondant
 * @return boolean
 */
	boolean createItCorrespondant(ItCorrespondant itCorrespondant);
	
	/**
	 *  Cette méthode permet la création complète d'un utilisateur (avec informations de niveau collaborater, uo et site)
	 * @param itCorrespondant
	 * @return boolean
	 */
		boolean createFullItCorrespondant(ItCorrespondant itCorrespondant);
/**
 * 	Mise à jour du password
 * @param idAnnuaire
 * @param oldPassword
 * @param newPassword
 * @return boolean
 */
	boolean updatePasswordItCorrespondant(String idAnnuaire, String oldPassword, String newPassword);
}
