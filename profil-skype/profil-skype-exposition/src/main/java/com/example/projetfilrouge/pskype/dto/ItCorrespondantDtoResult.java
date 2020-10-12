package com.example.projetfilrouge.pskype.dto;

import java.util.Set;

import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * Dto de retour d'un utilisateur 
 * @author Judicaël
 *
 */
public class ItCorrespondantDtoResult extends CollaboraterDto {
	
	private Set<RoleTypeEnum> roles;

	public ItCorrespondantDtoResult(){

	}

	public ItCorrespondantDtoResult(@Size(min = 1, max = 17) String collaboraterId, @Size(min = 1, max = 50) String lastName, @Size(min = 1, max = 50) String firstName, String deskPhoneNumber, String mobilePhoneNumber, @Email String mailAdress, @Size(min = 1) String orgaUnitCode, String orgaUnityType, String orgaShortLabel, @Size(min = 1) String siteCode, String siteName, String siteAddress, String sitePostalCode, String siteCity, Set<RoleTypeEnum> roles) {
		super(collaboraterId, lastName, firstName, deskPhoneNumber, mobilePhoneNumber, mailAdress, orgaUnitCode, orgaUnityType, orgaShortLabel, siteCode, siteName, siteAddress, sitePostalCode, siteCity);
		this.roles = roles;
	}

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}
	
}
