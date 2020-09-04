package com.example.projetfilrouge.pskype.domain;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Classe de dédiée au contrôle de données.
 * La classe est positionnée au sein de la couche Domain au lieu de la couche exposition pour éviter la duplication de code (plusieurs couches d'exposition)
 * Cette classe contient les méthodes statiques suivantes :
 * - Contrôle adresse email
 * - Controle numéro de téléphone
 * @author Judicaël
 *
 */
public class DataControl {
	
	/**
	 * cette méthode permet de vérifier la validité d'une adresse mail selon la norme RFC 2822
	 * 
	 * @param adresse mail
	 * @return un boolean true/false
	 */
		public static boolean isValidEmailAddress(String email) {
			   boolean result = true;
			   try {
			      InternetAddress emailAddr = new InternetAddress(email);
			      emailAddr.validate();
			   } catch (AddressException ex) {
			      result = false;
			   }
			   return result;
		}
}
