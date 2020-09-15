package com.example.projetfilrouge.pskype.domain.user;

import java.util.List;
import java.util.Set;
/**
 * Interface CIL pour la couche domaine.
 * Elle expose les méthodes suivantes :
 * - Création d'un CIL lorsque le collaborateur n'existe pas
 * - Attribution du rôle CIL à un collaborateur existant
 * - Mise à jour des rôles d'un CIL (annule et remplace les rôles existants)
 * - Suppression d'un CIL (ne supprime pas les informations de niveau collaborateur mais uniquement l'association entre un CIL et un rôle)
 * 
 * @author Judicaël
 *
 */
public interface IItCorrespondantDomain {
	/**
	 * Création d'un CIL avec le rôle user par défaut
	 * dans le cas ou le collaborateur n'existe pas
	 * @param itCorrespondant
	 */
	boolean createFull(ItCorrespondant itCorrespondant);
	/**
	 * Création d'un ItCorrespondant à partir d'un collaborateur existant
	 * @param itCorrespondant
	 * @return booléen
	 */
	boolean create(ItCorrespondant itCorrespondant);
	
	/**
	 * Attribution d'un rôle CIL à un collaborateur existant déjà en base
	 * (ils sont récupérés depuis la base de données)
	 * 
	 * @param idAnnuaire
	 * @param roles
	 * @param password
	 */
	boolean createRoleCILtoCollab(String idAnnuaire, Set<RoleTypeEnum> roles, String password);
	
	/**
	 * Mise à jour des rôles d'un CIL (annule et remplace les rôles existants)
	 * @param idAnnuaire
	 * @param roles
	 */
	boolean update(String idAnnuaire, Set<RoleTypeEnum> roles);
	
	/**
	 * Supprime physiquement de la base de données, le CIL passé en paramètre
	 * @param itCorrespondant
	 */
	boolean delete(ItCorrespondant itCorrespondant);
	
	/**
	 * Méthode qui retourne la liste de tous les CIL
	 * @return List<ItCorrespondant>
	 */
	List<ItCorrespondant> findAllItCorrespondant();
	
	/**
	 * Recherche multi-critères à partir des critères en entrée; chaque critère renseigné sera utilisé pour le filtre.
	 * lastName et FirstName acceptent des recherches partielles et ne sont pas sensibles à la casse.
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param deskPhone
	 * @param mobilePhone
	 * @param mailAddress
	 * @return List<ItCorrespondant>
	 */
	List<ItCorrespondant> findAllItCorrespondantFilters(String id, String lastName, String firstName, String deskPhone, String mobilePhone, String mailAddress);
	ItCorrespondant findItCorrespondantByCollaboraterId(String id);
/**
 * 
 * @param idAnnuaire
 * @param newPassword
 */
	boolean updatePassword(String idAnnuaire, String newPassword);

	/**
	 * Retourne le nombre d'it correspondant
	 * @return
	 */
    Long countItCorrespondant();
}
