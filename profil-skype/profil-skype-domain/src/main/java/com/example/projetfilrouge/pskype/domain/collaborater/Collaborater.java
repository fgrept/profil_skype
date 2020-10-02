package com.example.projetfilrouge.pskype.domain.collaborater;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


import com.example.projetfilrouge.pskype.domain.control.EmailControl;
import com.example.projetfilrouge.pskype.domain.control.PhoneControl;

/**
 * Cette classe permet contient la liste des collaborateurs.
 * Elle inclut aussi les informations du CIL et les administrateurs.
 * Les informations de cette classe sont issues d'un référentiel externe et ne sont pas modifiables par la transaction.
 * @author Judicaël
 * @version : V0.1
 *
 */

public class Collaborater extends Person {
	
	// Seules les annotations personnalisées sont conservées : utilisées par le batch de chargement
	// Elles sont aussi utilisées pour les tests du Domain
	private String collaboraterId;
	@PhoneControl
	private String deskPhoneNumber;
	@PhoneControl
	private String mobilePhoneNumber;
	@Email
	@EmailControl
	private String mailAdress;
	private OrganizationUnity orgaUnit; 
	
	public Collaborater() {
		
	}
	
	/**
	 * Constructeur permettant la création d'un utilisateur
	 * @param nom
	 * @param prenom
	 * @param id
	 * @param deskPhoneNumber
	 * @param mobilePhoneNumber
	 * @param mailAdress
	 */
	public Collaborater(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,String mailAdress, OrganizationUnity orgaUnit) {
		this.collaboraterId=id;
		this.deskPhoneNumber=deskPhoneNumber;
		this.mobilePhoneNumber=mobilePhoneNumber;
		this.mailAdress=mailAdress;
		this.setFirstNamePerson(prenom);
		this.setLastNamePerson(nom);
		this.orgaUnit=orgaUnit;
	}
	
//	public Collaborater(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,String mailAdress) {
//		this.collaboraterId=id;
//		this.deskPhoneNumber=deskPhoneNumber;
//		this.mobilePhoneNumber=mobilePhoneNumber;
//		this.mailAdress=mailAdress;
//		this.setFirstNamePerson(prenom);
//		this.setLastNamePerson(nom);
//	}
	
//	public Collaborater(@Size(max = 17) String collaboraterId, String deskPhoneNumber, String mobilePhoneNumber,
//			String mailAdress) {
//		super();
//		this.collaboraterId = collaboraterId;
//		this.deskPhoneNumber = deskPhoneNumber;
//		this.mobilePhoneNumber = mobilePhoneNumber;
//		this.mailAdress = mailAdress;
//	}

	public Collaborater(@Size(max = 17) String collaboraterId,String lastName, String firstName, OrganizationUnity organizationUnity){
		this.collaboraterId=collaboraterId;
		this.setFirstNamePerson(firstName);
		this.setLastNamePerson(lastName);
		this.orgaUnit=organizationUnity;
	}
	
	public String getCollaboraterId() {
		return collaboraterId;
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


	public OrganizationUnity getOrgaUnit() {
		return orgaUnit;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((collaboraterId == null) ? 0 : collaboraterId.hashCode());
		result = prime * result + ((deskPhoneNumber == null) ? 0 : deskPhoneNumber.hashCode());
		result = prime * result + ((mailAdress == null) ? 0 : mailAdress.hashCode());
		result = prime * result + ((mobilePhoneNumber == null) ? 0 : mobilePhoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collaborater other = (Collaborater) obj;
		if (collaboraterId == null) {
			if (other.collaboraterId != null)
				return false;
		}
		else
			if (!collaboraterId.equals(other.collaboraterId))
				return false;
		if (deskPhoneNumber == null) {
			if (other.deskPhoneNumber != null)
				return false;
		}
		else
			if (!deskPhoneNumber.equals(other.deskPhoneNumber))
			return false;
		if (mailAdress == null) {
			if (other.mailAdress != null)
				return false;
		}
		else
			if (!mailAdress.equals(other.mailAdress))
			return false;
		if (mobilePhoneNumber == null) {
			if (other.mobilePhoneNumber != null)
				return false;
		}
		else
			if (!mobilePhoneNumber.equals(other.mobilePhoneNumber))
			return false;
		return true;
	}

	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
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

	public void setOrgaUnit(OrganizationUnity orgaUnit) {
		this.orgaUnit = orgaUnit;
	}
}
