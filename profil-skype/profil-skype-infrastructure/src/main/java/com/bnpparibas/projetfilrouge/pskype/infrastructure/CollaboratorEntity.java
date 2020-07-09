package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 116453
 * Cette classe assure la persistence du collaborateur
 * 
 */
@Entity
public class CollaboratorEntity extends PersonEntity {
	
	private String collaboraterId;
	private String deskPhoneNumber;
	private String mobilePhoneNumber;
	private String mailAdress;
	
	public CollaboratorEntity() {
		super();
	}

	public String getCollaboraterId() {
		return collaboraterId;
	}

	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
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
	
	
}
