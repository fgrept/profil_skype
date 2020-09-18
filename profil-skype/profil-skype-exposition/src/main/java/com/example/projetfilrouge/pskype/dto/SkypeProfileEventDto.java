package com.example.projetfilrouge.pskype.dto;

import java.util.Date;

import com.example.projetfilrouge.pskype.domain.skypeprofile.TypeEventEnum;

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

	public SkypeProfileEventDto(){

	}

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

	
	public Date getDateEvent() {
		return dateEvent;
	}

	public TypeEventEnum getTypeEvent() {
		return typeEvent;
	}

	public String getCommentEvent() {
		return commentEvent;
	}

	public String getItCorrespondantFirstName() {
		return itCorrespondantFirstName;
	}

	public String getItCorrespondantLastName() {
		return itCorrespondantLastName;
	}

}
