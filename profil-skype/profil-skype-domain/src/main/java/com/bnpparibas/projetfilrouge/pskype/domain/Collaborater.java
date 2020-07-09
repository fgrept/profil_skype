package com.bnpparibas.projetfilrouge.pskype.domain;

import com.bnpparibas.projetfilrouge.pskype.domain.Person;

/**
 * Cette classe permet contient la liste des collaborateurs.
 * Elle inclut aussi les informations du CIL et les administrateurs
 * Les informations de cette classe sont issues d'un référentiel externe et ne sont pas modifiables par la transaction.
 * @author Judicaël
 * @version : V0.1
 *
 */
public class Collaborater extends Person {
	
	private String collaboraterId;
	private String deskPhoneNumber;
	private String mobilePhoneNumber;
	private String mailAdress;
	
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
	public Collaborater(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,String mailAdress) {
		this.collaboraterId=id;
		this.deskPhoneNumber=deskPhoneNumber;
		this.mobilePhoneNumber=mobilePhoneNumber;
		this.mailAdress=mailAdress;
		this.setFirstNamePerson(prenom);
		this.setLastNamePerson(nom);
	}
	

	
	public String getCollaboraterId() {
		return collaboraterId;
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
