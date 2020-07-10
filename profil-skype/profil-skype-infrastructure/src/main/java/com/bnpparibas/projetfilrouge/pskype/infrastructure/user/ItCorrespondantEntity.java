package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

@Entity
@DiscriminatorValue("ItCorrespondant")
public class ItCorrespondantEntity extends CollaboraterEntity {
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<RoleTypeEnum> roles = new HashSet<>();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date dateLastUpdate = new Date();
	
	public ItCorrespondantEntity() {
		
	}
	
	public ItCorrespondantEntity(String nom, String prenom, String id, String deskPhoneNumber, String mobilePhoneNumber,String mailAdress) {
		super(nom, prenom, id, deskPhoneNumber, mobilePhoneNumber, mailAdress);
		
	}

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}

	public Date getDateLastUpdate() {
		return dateLastUpdate;
	}

	
}
