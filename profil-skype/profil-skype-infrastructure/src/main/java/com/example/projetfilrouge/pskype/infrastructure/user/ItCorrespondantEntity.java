package com.example.projetfilrouge.pskype.infrastructure.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import com.example.projetfilrouge.pskype.infrastructure.collaborater.CollaboraterEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entité CIL, créé par l'administrateur
 * @author Judicaël
 * Spring Security : ajout du password
 */
@Entity
@DiscriminatorValue("ItCorrespondant")
public class ItCorrespondantEntity  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser;
	
	// cet id est l'identifiant de connexion, pas forcément l'identifiant annuaire
	// mais dans le service d'attribution de role cil à un collaborateur, on set cette donnée avec l'id annuaire.
	private String itCorrespondantId;
	private String encryptedPassword;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<RoleTypeEnum> roles = new HashSet<>();
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date dateLastUpdate;

	@OneToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.PERSIST)
	private CollaboraterEntity collaborater;
	
	public ItCorrespondantEntity() {
		
	}
	
	public ItCorrespondantEntity(CollaboraterEntity collaborater, String user, String password) {
		this.collaborater=collaborater;
		this.itCorrespondantId=user;
		this.encryptedPassword=password;
	}
	
	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	
	public String getItCorrespondantId() {
		return itCorrespondantId;
	}

	public void setItCorrespondantId(String itCorrespondantId) {
		this.itCorrespondantId = itCorrespondantId;
	}

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

	public CollaboraterEntity getCollaborater() {
		return collaborater;
	}

	public void setCollaborater(CollaboraterEntity collaborater) {
		this.collaborater = collaborater;
	}
	
}
