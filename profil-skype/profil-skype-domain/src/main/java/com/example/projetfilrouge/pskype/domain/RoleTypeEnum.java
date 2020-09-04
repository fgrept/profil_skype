package com.example.projetfilrouge.pskype.domain;
/**
 * Liste des rôles dans l'application :
 * ROLE_USER : permet uniquement la consultation d'un collaborateur du profil skype et des événements associés.
 * ROLE_RESP : contient ROLE_USER et permet la modification, la suppression d'un profil Skype.
 * ROLE_ADMIN : contient le ROLE_RESP et permet la création d'un CIL et de lui affectuer un rôle (ROLE_USER ou ROLE_RESO)
 * @author Judicël
 * @version V0.1
 *
 */
public enum RoleTypeEnum {
	ROLE_USER, ROLE_RESP, ROLE_ADMIN
}
