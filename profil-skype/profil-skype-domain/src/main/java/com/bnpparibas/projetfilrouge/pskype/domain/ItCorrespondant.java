package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Classe des CIL, des resp CIL et de admin
 * Nous ne faisons pas de distinction entre ces 3 rôles, en l'absence d'attribut spécifique
 * @author Judicael
 * @version V0.1
 *
 */
public class ItCorrespondant extends Collaborater {

	//Liste des rôles possibles : user, resp ou admin
	
	private Set<RoleTypeEnum> roles;
	private Date dateLastUpdate;


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
			String mailAdress) {
		super(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress);
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
