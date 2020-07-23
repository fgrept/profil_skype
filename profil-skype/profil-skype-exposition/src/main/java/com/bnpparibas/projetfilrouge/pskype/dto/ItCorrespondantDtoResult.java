package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Set;

import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

/**
 * Dto de retour d'un utilisateur 
 * @author Judicaël
 *
 */
public class ItCorrespondantDtoResult extends CollaboraterDto {
	
	private Set<RoleTypeEnum> roles;

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	
}
