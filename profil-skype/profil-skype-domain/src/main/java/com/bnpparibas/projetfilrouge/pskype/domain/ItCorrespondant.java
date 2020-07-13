package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Classe des CIL, des resp CIL et de admin
 * @author Judicael
 * @version V0.1
 *
 */
public class ItCorrespondant extends Collaborater {

	private Set<RoleTypeEnum> roles;
	private Date dateLastUpdate;

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
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
			String mailAdress) {
		super(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress);
		this.roles =  new HashSet<RoleTypeEnum>();
		
		// TODO Auto-generated constructor stub
	}
	/**
	 * Ajout d'un rôle parmi l'énumération.
	 * Les rôles étant inclusifs, l'ajout d'un rôle supprimer tous les autres.
	 * @param role
	 */
	
	public void addRole(RoleTypeEnum role) {
		roles.add(role);
	}

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	
	public void removeRole (RoleTypeEnum role) {
		roles.remove(role);
	}
	
}
