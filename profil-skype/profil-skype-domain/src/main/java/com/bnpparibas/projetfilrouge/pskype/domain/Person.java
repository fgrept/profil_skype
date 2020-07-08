package com.bnpparibas.projetfilrouge.pskype.domain;

/**
 * Classe abstraite d'une personne
 * @author Judicaël
 * @version V0.1
 *
 */
public abstract class Person {
	private String nomPerson;
	private String prenomPerson;
	
	public String getNomPerson() {
		return nomPerson;
	}
	public void setNomPerson(String nomPerson) {
		this.nomPerson = nomPerson;
	}
	public String getPrenomPerson() {
		return prenomPerson;
	}
	public void setPrenomPerson(String prenomPerson) {
		this.prenomPerson = prenomPerson;
	}
	
}
