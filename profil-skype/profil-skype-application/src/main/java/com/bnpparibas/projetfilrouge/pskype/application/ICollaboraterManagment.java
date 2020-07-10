package com.bnpparibas.projetfilrouge.pskype.application;
import java.util.List;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;;

/**
 * 
 * Liste des méthodes Collaborater disponibles pour la couche exposition
 * @author Judicaël
 *
 */

public interface ICollaboraterManagment {
	void createCollaborater(String nom, String prenom, String id, String deskPhoneNumber2, String mobilePhoneNumber,
			String mailAdress);
	List<Collaborater> listCollaborater();
}
