
package com.example.projetfilrouge.pskype.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.example.projetfilrouge.pskype.domain.control.EmailControl;
import com.example.projetfilrouge.pskype.domain.control.PhoneControl;

/**
 * Classe dédiée à l'ItCollborater
 * Elle contient les critères de recherches 
 * @author Judicaël
 *
 */
public class CollaboraterDto {
	
	@Size(min = 1, max = 17)
	private String collaboraterId;
	@Size(min = 1, max = 50)
	private String lastName;
	@Size(min = 1, max = 50)
	private String firstName;
	@PhoneControl
	private String deskPhoneNumber;
	@PhoneControl
	private String mobilePhoneNumber;
	@EmailControl
	@Email
	private String mailAdress;
	@Size(min = 1)
	private String orgaUnitCode;
	private String orgaUnityType;
	private String orgaShortLabel;
	@Size(min = 1)
	private String siteCode;
	private String siteName;
	private String siteAddress;
	private String sitePostalCode;
	private String siteCity;

	public CollaboraterDto(){

	}

	public CollaboraterDto(@Size(min = 1, max = 17) String collaboraterId, @Size(min = 1, max = 50) String lastName, @Size(min = 1, max = 50) String firstName, String deskPhoneNumber, String mobilePhoneNumber, @Email String mailAdress, @Size(min = 1) String orgaUnitCode, String orgaUnityType, String orgaShortLabel, @Size(min = 1) String siteCode, String siteName, String siteAddress, String sitePostalCode, String siteCity) {
		this.collaboraterId = collaboraterId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.deskPhoneNumber = deskPhoneNumber;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.mailAdress = mailAdress;
		this.orgaUnitCode = orgaUnitCode;
		this.orgaUnityType = orgaUnityType;
		this.orgaShortLabel = orgaShortLabel;
		this.siteCode = siteCode;
		this.siteName = siteName;
		this.siteAddress = siteAddress;
		this.sitePostalCode = sitePostalCode;
		this.siteCity = siteCity;
	}

	public String getOrgaUnityType() {
		return orgaUnityType;
	}

	public String getOrgaShortLabel() {
		return orgaShortLabel;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public String getSitePostalCode() {
		return sitePostalCode;
	}

	public String getSiteCity() {
		return siteCity;
	}

	public String getOrgaUnitCode() {
		return orgaUnitCode;
	}

	public String getCollaboraterId() {
		return collaboraterId;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getDeskPhoneNumber() {
		return deskPhoneNumber;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public String getMailAdress() {
		return mailAdress;
	}

}
