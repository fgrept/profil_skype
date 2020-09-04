package com.example.projetfilrouge.pskype.dto;

import com.example.projetfilrouge.pskype.domain.RoleTypeEnum;

/**
 * Dto de recherche d'un utilisateur à partir de ses données collaborateur et d'un rôle
 * @author Judicaël
 *
 */
public class ItCorrespondantDtoSearch extends CollaboraterDto {
	
	private RoleTypeEnum role;

	public RoleTypeEnum getRole() {
		return role;
	}

	public void setRole(RoleTypeEnum role) {
		this.role = role;
	}
	
}
