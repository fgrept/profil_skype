package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.Date;

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
	
	private SkypeProfile skypeProfile;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentEvent == null) ? 0 : commentEvent.hashCode());
		result = prime * result + ((typeEvent == null) ? 0 : typeEvent.hashCode());
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
		SkypeProfileEvent other = (SkypeProfileEvent) obj;
		if (commentEvent == null) {
			if (other.commentEvent != null)
				return false;
		} else if (!commentEvent.equals(other.commentEvent))
			return false;
		if (typeEvent != other.typeEvent)
			return false;
		return true;
	}
	
}
