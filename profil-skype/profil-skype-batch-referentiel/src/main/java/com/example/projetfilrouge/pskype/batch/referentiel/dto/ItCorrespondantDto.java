package com.example.projetfilrouge.pskype.batch.referentiel.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ItCorrespondantDto {
	
	@Size(max = 17)
	private String idAnnuaire;
	private String password;
	@Pattern(regexp = "^(ROLE_ADMIN|ROLE_RESP|ROLE_USER)$")
	private String role;
	
	public String getIdAnnuaire() {
		return idAnnuaire;
	}
	public void setIdAnnuaire(String idAnnuaire) {
		this.idAnnuaire = idAnnuaire;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
