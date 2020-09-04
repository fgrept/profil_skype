package com.example.projetfilrouge.pskype.application;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projetfilrouge.pskype.application.email.MailSenderProfile;
import com.example.projetfilrouge.pskype.domain.Collaborater;
import com.example.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.example.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.example.projetfilrouge.pskype.domain.ItCorrespondant;
import com.example.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.example.projetfilrouge.pskype.domain.exception.ExceptionListEnum;
import com.example.projetfilrouge.pskype.domain.exception.NotFoundException;

/**
 * Service exposant les méthodes d'interfaction avec le CIL (US007 et US010)
 * Spring Security : ajout du password
 * @author Judicaël
 * @version V0.1
 *
 */

@Service
@Transactional
public class ItCorrespondantManagementImpl implements IItCorrespondantManagment {

	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantManagementImpl.class);
	
	@Autowired
	private IItCorrespondantDomain itCorrespodantDomain;
	
	@Autowired
	private ICollaboraterDomain collaboraterDomain;
	
	@Autowired
	private ISkypeProfileEventDomain eventDomain;
	
	@Autowired
	private MailSenderProfile mailSenderProfile;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * Cette méthode permet la création d'un utilisateur avec le rôle ROLE_USER par défaut (US010)
	 * @param ItCorrespondant itCorrespondant
	 * @return boolean
	 * @author Judicaël
	 * @version V0.2
	 */
	@Override
	public boolean createItCorrespondant(String idAnnuaire, Set<RoleTypeEnum> roles) {

		Random rand = new Random();
		String passwordBrut = Integer.toString(rand.nextInt(999999)); 
		String passwordCode = passwordEncoder.encode(passwordBrut);
		logger.info("création du mot de passe : " + passwordBrut + " pour le collaborateur : " + idAnnuaire);
		
		Collaborater collab = collaboraterDomain.findByCollaboraterId(idAnnuaire);
		
		if (collab == null) {
			String msg = "collaborateur "  + idAnnuaire + " inexistant pour la création du cil";
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND11, msg);
		}
		
		String emailToSend = collab.getMailAdress();
		
		try {
			mailSenderProfile.sendMail("Bonjour," + "\n\n" + "Le mot de passe pour accéder à l'application est : " + "\n\n" + 
						passwordBrut + "\n\n" +
						"Merci de le modifier à la première connexion", emailToSend);
		} catch (Exception e) {
			logger.error("Procédure d'envoi par mail du mot de passe en échec");
			return false;
		}
		
		return itCorrespodantDomain.createRoleCILtoCollab(idAnnuaire, roles, passwordCode);
	}
	
	
	/**
	 *  Cette méthode permet la création complète d'un utilisateur (avec informations de niveau collaborater, uo et site)
	 * @param itCorrespondant
	 * @return boolean
	 */
	@Override
	public boolean createFullItCorrespondant(ItCorrespondant itCorrespondant) {
		logger.debug("Création full it Correspondant "+itCorrespondant.getCollaboraterId());
		itCorrespondant.addRole(RoleTypeEnum.ROLE_USER);
		String password = itCorrespondant.getPassword();
		itCorrespondant.setPassword(passwordEncoder.encode(password));

		return itCorrespodantDomain.createFull(itCorrespondant);
	}

	/**
	 * Cette liste retourne l'ensemble des utilisateurs tout rôle confondu (US007)
	 * @return List<ItCorrespondant>
	 * @author Judicaël
	 * @version V0.1
	 * 
	 */
	@Override
	public List<ItCorrespondant> listItCorrespondant() {
		
		return itCorrespodantDomain.findAllItCorrespondant();
	}

	/**
	 * Mise à jour du rôle de l'utilisateur (user, resp ou admin)
	 * L'ajout d'un rôle annule et remplace le précédent
	 * @param String id annuaire du l'utilisateur
	 * @param RoleTypeEnum nouveau role de l'utilisateur
	 * @return boolean
	 */
	@Override
	public boolean updateRoleItCorrespondant(String idAnnuaire, Set<RoleTypeEnum> roles) {
		
		//Le contrôle de l'existence est réalisé dans la couche de persistence
		return itCorrespodantDomain.update(idAnnuaire, roles);
	}

	/**
	 * Suppression d'un CIL (US008)
	 * @param String idAnnuaire
	 * @return boolean
	 */
	@Override
	public boolean deleteItCorrespondant(String idAnnuaire) {
		
		ItCorrespondant itCorrespondant = itCorrespodantDomain.findItCorrespondantByCollaboraterId(idAnnuaire);
		if (itCorrespondant == null) {
			String msg = "Utilisateur non trouvé en base, id "+idAnnuaire;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND9, msg);
		}else {
			if (eventDomain.findAllEventByItCorrespondantId(itCorrespondant.getCollaboraterId()) != null) {
				
				if (!eventDomain.updateEventItCorrespondant(itCorrespondant,null)) {
					return false;
				}
			}
			return itCorrespodantDomain.delete(itCorrespondant);
		}
	}


	@Override
	public List<ItCorrespondant> listItCorrespondantFilters(ItCorrespondant itCorrespondant) {
		
		return itCorrespodantDomain.findAllItCorrespondantFilters(itCorrespondant.getCollaboraterId(),itCorrespondant.getLastNamePerson(), itCorrespondant.getFirstNamePerson(), 
				itCorrespondant.getDeskPhoneNumber(),itCorrespondant.getMobilePhoneNumber(),itCorrespondant.getMailAdress());
	}
	/**
	 * Mise à jour du password d'un CIL à partir de son id annuaire
	 * Dédié à Spring Security
	 * @param idAnnuaire
	 * @param password
	 * @return boolean
	 */
	@Override
	public boolean updatePasswordItCorrespondant(String idAnnuaire, String oldPassword, String newPassword) {
		ItCorrespondant itCorrespondant = itCorrespodantDomain.findItCorrespondantByCollaboraterId(idAnnuaire);
		if (itCorrespondant == null) {
			String msg = "Utilisateur non trouvé en base, id "+idAnnuaire;
			logger.error(msg);
			throw new NotFoundException(ExceptionListEnum.NOTFOUND7, msg);
		}else {
			if (passwordEncoder.matches(oldPassword, itCorrespondant.getPassword())) {
				String newEncryptedPassword = passwordEncoder.encode(newPassword);
				return itCorrespodantDomain.updatePassword(idAnnuaire,newEncryptedPassword);
			}else {
				String msg = "ancien mot de passe incorrect : "+oldPassword;
				logger.error(msg);
				throw new NotFoundException(ExceptionListEnum.NOTFOUND8, msg);	
			}
		}	
	}
}
