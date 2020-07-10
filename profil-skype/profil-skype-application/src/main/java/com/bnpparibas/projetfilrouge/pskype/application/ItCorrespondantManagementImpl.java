package com.bnpparibas.projetfilrouge.pskype.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

/**
 * Service exposant les méthodes d'interfaction avec le CIL (US007 et US010)
 * @author Judicaël
 * @version V0.1
 *
 */

@Service
public class ItCorrespondantManagementImpl implements IItCorrespondantManagment {

	@Autowired
	private IItCorrespondantDomain itCorrespodantDomain;
	
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
	@Override
	public void createCIL(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,
			String mailAdress) {
		ItCorrespondant itCorrespondant = new ItCorrespondant(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress);
		itCorrespondant.addRole(RoleTypeEnum.ROLE_USER);
		itCorrespodantDomain.create(itCorrespondant);
	}

	/**
	 * Cette liste retourne l'ensemble des CIL tout rôle confondu (US007)
	 * @return List<ItCorrespondant>
	 * @author Judicaël
	 * @version V0.1
	 * 
	 */
	@Override
	public List<ItCorrespondant> listItCorrespondant() {
		// TODO Auto-generated method stub
		return itCorrespodantDomain.findAllItCorrespondant();
	}

	/**
	 * Mise à jour du rôle du CIL (user, resp ou admin)
	 * L'ajout d'un rôle annule et remplace le précédent
	 * @param String id annuaire du CIL
	 * @param RoleTypeEnum nouveau role du CIL
	 */
	@Override
	public void updateRoleCIL(String idAnnuaire, RoleTypeEnum role) {
		
		ItCorrespondant itCorrespondant = itCorrespodantDomain.findItCorrespondantByCollaboraterId(idAnnuaire);
		itCorrespondant.addRole(role);
		itCorrespodantDomain.update(itCorrespondant);
		
	}

	/**
	 * Suppression d'un CIL
	 */
	@Override
	public void deleteCIL(String idAnnuaire) {
		// TODO Auto-generated method stub
		
	}

}
