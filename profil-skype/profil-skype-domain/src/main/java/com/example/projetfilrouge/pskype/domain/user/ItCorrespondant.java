package com.example.projetfilrouge.pskype.domain.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;


/**
 * Classe des CIL, des resp CIL et de admin
 * Nous ne faisons pas de distinction entre ces 3 rôles, en l'absence d'attribut spécifique
 * @author Judicael
 * @version V0.1
 * Spring Securtiy : ajout du password
 */
public class ItCorrespondant extends Collaborater {

	//Liste des rôles possibles : user, resp ou admin
	
	private Set<RoleTypeEnum> roles;
	private Date dateLastUpdate;
	private String encryptedPassword;

	public String getPassword() {
		return encryptedPassword;
	}

	public void setPassword(String password) {
		this.encryptedPassword = password;
	}

	public Date getDateLastUpdate() {
		return dateLastUpdate;
	}

	public void setDateLastUpdate(Date dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}

	public ItCorrespondant() {
		this.roles = null;
		
	}
	
	public ItCorrespondant(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,
			String mailAdress, OrganizationUnity orgaUnity) {
		super(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress, orgaUnity);
		this.roles =  new HashSet<RoleTypeEnum>();
		

	}
	/**
	 * Ajout d'un rôle parmi l'énumération.
	 * Les rôles étant inclusifs, l'ajout d'un rôle supprimer tous les autres.
	 * @param role
	 */
	
	public void addRole(RoleTypeEnum role) {
		roles.add(role);
	}

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	
	public void removeRole (RoleTypeEnum role) {
		roles.remove(role);
	}
	
}
