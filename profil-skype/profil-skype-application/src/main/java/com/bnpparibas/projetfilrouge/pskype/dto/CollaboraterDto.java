
package com.bnpparibas.projetfilrouge.pskype.dto;

import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

/**
 * Classe dédiée à l'ItCollborater
 * Elle contient les critères de recherches 
 * @author Judicaël
 *
 */
public class CollaboraterDto {
	
	private String collaboraterId;
	private String lastName;
	private String firstName;
	private String deskPhoneNumber;
	private String mobilePhoneNumber;
	private String mailAdress;
	private String orgaUnitCode;
	
	public String getOrgaUnitCode() {
		return orgaUnitCode;
	}
	public void setOrgaUnitCode(String orgaUnitCode) {
		this.orgaUnitCode = orgaUnitCode;
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
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setDeskPhoneNumber(String deskPhoneNumber) {
		this.deskPhoneNumber = deskPhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public void setMailAdress(String mailAdress) {
		this.mailAdress = mailAdress;
	}
	
}
