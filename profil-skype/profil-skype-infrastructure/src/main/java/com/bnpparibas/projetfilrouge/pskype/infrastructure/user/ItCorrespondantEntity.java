package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;

@Entity
@DiscriminatorValue("ItCorrespondant")
public class ItCorrespondantEntity extends CollaboraterEntity {
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<RoleTypeEnum> roles = new HashSet<>();
	
	private Date dateLastUpdate;

	public Set<RoleTypeEnum> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleTypeEnum> roles) {
		this.roles = roles;
	}

	public Date getDateLastUpdate() {
		return dateLastUpdate;
	}

	public void setDateLastUpdate(Date dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}
	
}
