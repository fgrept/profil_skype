package com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.UpdateTimestamp;

import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntity;
/**
 * 
 * Entité du Profil Skype événement
 * @author 479680
 *
 */
@Entity
public class SkypeProfileEventEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSkypeProfileEvent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date dateEvent;
	@Enumerated(EnumType.STRING)
	private TypeEventEnum typeEvent;
	private String commentEvent;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	
	private SkypeProfileEntity skypeProfile;
	//Suppression du NotNull
	//Il est en effet tout à fait possible de supprimer un ItCorrespondant alors que l'event existe toujours.
	@ManyToOne(fetch = FetchType.EAGER)
	private ItCorrespondantEntity itCorrespondant;
	
	public Long getIdSkypeProfileEvent() {
		return idSkypeProfileEvent;
	}
	public void setIdSkypeProfileEvent(Long idSkypeProfileEvent) {
		this.idSkypeProfileEvent = idSkypeProfileEvent;
	}
	public Date getDateEvent() {
		return dateEvent;
	}
	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}
	public TypeEventEnum getTypeEvent() {
		return typeEvent;
	}
	public void setTypeEvent(TypeEventEnum typeEvent) {
		this.typeEvent = typeEvent;
	}
	public String getCommentEvent() {
		return commentEvent;
	}
	public void setCommentEvent(String commentEvent) {
		this.commentEvent = commentEvent;
	}
	public SkypeProfileEntity getSkypeProfile() {
		return skypeProfile;
	}
	public void setSkypeProfile(SkypeProfileEntity skypeProfile) {
		this.skypeProfile = skypeProfile;
	}
	public ItCorrespondantEntity getItCorrespondant() {
		return itCorrespondant;
	}
	public void setItCorrespondant(ItCorrespondantEntity itCorrespondant) {
		this.itCorrespondant = itCorrespondant;
	}

}
