package com.example.projetfilrouge.pskype.domain;

/**
 * Classe abstraite d'une personne
 * @author Judicaël
 * @version V0.1
 *
 */
public abstract class Person {

	private String lastNamePerson;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstNamePerson == null) ? 0 : firstNamePerson.hashCode());
		result = prime * result + ((lastNamePerson == null) ? 0 : lastNamePerson.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstNamePerson == null) {
			if (other.firstNamePerson != null)
				return false;
		} else if (!firstNamePerson.equals(other.firstNamePerson))
			return false;
		if (lastNamePerson == null) {
			if (other.lastNamePerson != null)
				return false;
		} else if (!lastNamePerson.equals(other.lastNamePerson))
			return false;
		return true;
	}
	

	
}
