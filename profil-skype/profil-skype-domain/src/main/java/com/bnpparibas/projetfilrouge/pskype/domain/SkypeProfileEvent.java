package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
/**
 * Liste des événements pour un profil skype donné
 * Les événènements sont le résultat d'une action et sont non modifiables après
 * @author Judicaël
 *
 */
public class SkypeProfileEvent {
	private Date dateEvent;
	private TypeEventEnum typeEvent;
	private String commentEvent;
	
	@NotNull
	private SkypeProfile skypeProfile;
	@NotNull
	private ItCorrespondant itCorrespondant;
	
	public SkypeProfileEvent() {
		
	}
	public SkypeProfileEvent(String comment,SkypeProfile profile,ItCorrespondant CIL, TypeEventEnum typeEvent) {
		
		this.commentEvent=comment;
		this.skypeProfile=profile;
		this.itCorrespondant=CIL;
		this.typeEvent=typeEvent;
	}

	public TypeEventEnum getTypeEvent() {
		return typeEvent;
	}


	public Date getDateEvent() {
		return dateEvent;
	}

	public String getCommentEvent() {
		return commentEvent;
	}

	public SkypeProfile getSkypeProfile() {
		return skypeProfile;
	}

	public ItCorrespondant getItCorrespondant() {
		return itCorrespondant;
	}
	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}
	public void setTypeEvent(TypeEventEnum typeEvent) {
		this.typeEvent = typeEvent;
	}
	public void setCommentEvent(String commentEvent) {
		this.commentEvent = commentEvent;
	}
	public void setSkypeProfile(SkypeProfile skypeProfile) {
		this.skypeProfile = skypeProfile;
	}
	public void setItCorrespondant(ItCorrespondant itCorrespondant) {
		this.itCorrespondant = itCorrespondant;
	}
	
}
