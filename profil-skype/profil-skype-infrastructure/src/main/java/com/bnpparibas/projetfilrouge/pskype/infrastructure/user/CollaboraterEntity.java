package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
/**
 * 
 * Entité des collaborateurs
 * @author Judicaël
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Collaborater")
public class CollaboraterEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser;

	private String collaboraterId;
	private String lastName;
	private String firstName;
	private String deskPhoneNumber;
	private String mobilePhoneNumber;
	private String mailAdress;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	private OrganizationUnityEntity orgaUnit;
	
	
	public CollaboraterEntity() {
		
	}
	public CollaboraterEntity(String nom, String prenom, String id, String deskPhoneNumber2, String mobilePhoneNumber2,
			String mailAdress2) {
		this.collaboraterId = id;
		this.firstName=prenom;
		this.lastName=nom;
		this.deskPhoneNumber=deskPhoneNumber2;
		this.mobilePhoneNumber=mobilePhoneNumber2;
		this.mailAdress=mailAdress2;
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
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
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
	public OrganizationUnityEntity getOrgaUnit() {
		return orgaUnit;
	}
	public void setOrgaUnit(OrganizationUnityEntity orgaUnit) {
		this.orgaUnit = orgaUnit;
	}
	@Override
	public String toString() {
		return "CollaboraterEntity [idUser=" + idUser + ", collaboraterId=" + collaboraterId + ", lastName=" + lastName
				+ ", firstName=" + firstName + ", deskPhoneNumber=" + deskPhoneNumber + ", mobilePhoneNumber="
				+ mobilePhoneNumber + ", mailAdress=" + mailAdress + ", orgaUnit=" + orgaUnit.toString() + "]";
	}
	
	
}
