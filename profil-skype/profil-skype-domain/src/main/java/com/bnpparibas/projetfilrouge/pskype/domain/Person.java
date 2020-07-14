package com.bnpparibas.projetfilrouge.pskype.domain;

import javax.validation.constraints.Size;

/**
 * Classe abstraite d'une personne
 * @author Judicaël
 * @version V0.1
 *
 */
public abstract class Person {
	@Size(max = 50)
	private String lastNamePerson;
	@Size(max = 50)
	private String firstNamePerson;
	public String getLastNamePerson() {
		return lastNamePerson;
	}
	public void setLastNamePerson(String lastNamePerson) {
		this.lastNamePerson = lastNamePerson;
	}
	public String getFirstNamePerson() {
		return firstNamePerson;
	}
	public void setFirstNamePerson(String firstNamePerson) {
		this.firstNamePerson = firstNamePerson;
	}
	

	
}
