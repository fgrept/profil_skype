package com.example.projetfilrouge.pskype.dto;

import java.util.Set;

import javax.validation.constraints.Size;


import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;


public class ItCorrespondantDtoCreate {
	
	@Size(min = 1)
	private String collaboraterId;
	
	private Set<RoleTypeEnum> roles;

	public ItCorrespondantDtoCreate() {
	}

	public ItCorrespondantDtoCreate(@Size(min = 1) String collaboraterId, Set<RoleTypeEnum> roles) {
		this.collaboraterId = collaboraterId;
		this.roles = roles;
	}

	public String getCollaboraterId() {
		return collaboraterId;
	}
	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	
}
