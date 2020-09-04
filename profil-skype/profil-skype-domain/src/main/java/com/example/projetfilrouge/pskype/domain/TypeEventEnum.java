package com.example.projetfilrouge.pskype.domain;
/**
 * Cette énumération contient la liste des événements possibles sur un profil skype
 * CREATION : à la création du profil skype uniquement
 * MODIFICATION : dès qu'un des attributs du profil skype est modifié à l'exception du status.
 * DESACTIVATION : modification du statut à "disabled"
 * ACTIVATON : modification du status à "enabled"
 * @author Judicael
 *
 */
public enum TypeEventEnum {
	CREATION,MODIFICATION,ACTIVATION,DESACTIVATION
}
