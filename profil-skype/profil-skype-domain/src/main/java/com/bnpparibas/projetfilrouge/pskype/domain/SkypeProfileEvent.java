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
	private ItCorrespendent itCorrespondant;
	
	public SkypeProfileEvent(Date date,String comment,SkypeProfile profile,ItCorrespendent CIL, TypeEventEnum typeEvent) {
		this.dateEvent=date;
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

	public ItCorrespendent getItCorrespondant() {
		return itCorrespondant;
	}
	
}
