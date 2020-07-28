package com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.control.EmailControl;

public class CollaboraterDtoBatch {
	
	@Size(max = 17)
	private String collaboraterId;
	@Size(max = 50)
	private String lastName;
	@Size(max = 50)
	private String firstName;
	// en attente de fonctionnement du test sur le n° de tel
	private String deskPhoneNumber;
	private String mobilePhoneNumber;
	@EmailControl
	private String mailAdress;
	@NotNull
	@Size(min = 1)
	private String orgaUnitCode;
	
	
	public CollaboraterDtoBatch() {
		
	}
	public String getCollaboraterId() {
		return collaboraterId;
	}
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getDeskPhoneNumber() {
		return deskPhoneNumber;
	}
	public void setDeskPhoneNumber(String deskPhoneNumber) {
		this.deskPhoneNumber = deskPhoneNumber;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public String getMailAdress() {
		return mailAdress;
	}
	public void setMailAdress(String mailAdress) {
		this.mailAdress = mailAdress;
	}
	public String getOrgaUnitCode() {
		return orgaUnitCode;
	}
	public void setOrgaUnitCode(String orgaUnitCode) {
		this.orgaUnitCode = orgaUnitCode;
	}
	
}
