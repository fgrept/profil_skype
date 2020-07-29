package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Set;

import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;


public class ItCorrespondantDtoCreate {
	
	@Size(min = 1)
	private String collaboraterId;
	
	private Set<RoleTypeEnum> roles;
	
	public String getCollaboraterId() {
		return collaboraterId;
	}
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
	}
	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}
	
}
