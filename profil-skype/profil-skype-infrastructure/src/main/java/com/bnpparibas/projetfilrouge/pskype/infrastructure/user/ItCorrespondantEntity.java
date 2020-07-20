package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;


import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
/**
 * Entité CIL, créé par l'administrateur
 * @author Judicaël
 * Spring Security : ajout du password
 */
@Entity
@DiscriminatorValue("ItCorrespondant")
public class ItCorrespondantEntity extends CollaboraterEntity {
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<RoleTypeEnum> roles = new HashSet<>();
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date dateLastUpdate;

	private String encryptedPassword;
	
	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}
	
	public void addRoles(RoleTypeEnum role) {
		this.roles.add(role);
	}

	public Date getDateLastUpdate() {
		return dateLastUpdate;
	}
	
	@PrePersist
	@PreUpdate
	public void setDateLastUpdate() {
		dateLastUpdate = new Date();
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public void setDateLastUpdate(Date dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}


	
}
