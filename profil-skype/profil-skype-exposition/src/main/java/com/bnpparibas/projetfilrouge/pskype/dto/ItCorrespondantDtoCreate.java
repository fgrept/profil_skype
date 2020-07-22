package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Set;

import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;


public class ItCorrespondantDtoCreate extends CollaboraterDto {
	
	private String password;
	private Set<RoleTypeEnum> roles;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}
	
}
