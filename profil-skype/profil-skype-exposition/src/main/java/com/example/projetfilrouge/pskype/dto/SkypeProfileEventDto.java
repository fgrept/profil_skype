package com.example.projetfilrouge.pskype.dto;

import java.util.Date;

import com.example.projetfilrouge.pskype.domain.TypeEventEnum;

/**
 * Classe exposée à l'utilisateur sur les évènements
 *
 * @author 116453
 *
 */
public class SkypeProfileEventDto {
		
	private Date dateEvent;
	private TypeEventEnum typeEvent;
	private String commentEvent;	
	private String itCorrespondantId ;
	private String itCorrespondantFirstName;
	private String itCorrespondantLastName;

	public SkypeProfileEventDto(Date dateEvent, TypeEventEnum typeEvent, String commentEvent, String itCorrespondantId,
			String itCorrespondantFirstName, String itCorrespondantLastName) {
		super();
		this.dateEvent = dateEvent;
		this.typeEvent = typeEvent;
		this.commentEvent = commentEvent;
		this.itCorrespondantId = itCorrespondantId;
		this.itCorrespondantFirstName = itCorrespondantFirstName;
		this.itCorrespondantLastName = itCorrespondantLastName;
	}

	public String getItCorrespondantId() {
		return itCorrespondantId;
	}

	public void setItCorrespondantId(String itCorrespondantId) {
		this.itCorrespondantId = itCorrespondantId;
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

	public String getItCorrespondantFirstName() {
		return itCorrespondantFirstName;
	}

	public void setItCorrespondantFirstName(String itCorrespondantFirstName) {
		this.itCorrespondantFirstName = itCorrespondantFirstName;
	}

	public String getItCorrespondantLastName() {
		return itCorrespondantLastName;
	}

	public void setItCorrespondantLastName(String itCorrespondantLastName) {
		this.itCorrespondantLastName = itCorrespondantLastName;
	}

}
