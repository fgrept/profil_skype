package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;

public class SkypeProfileEventDto {
	
	
	private Date dateEvent;
	private TypeEventEnum typeEvent;
	private String commentEvent;
	
	private SkypeProfileDto skypeProfile;

	private String itCorrespondantId ;


	public SkypeProfileEventDto(SkypeProfileDto skypeProfile,TypeEventEnum typeEvent, String commentEvent,String itCorrespondantId) {
		super();
		this.skypeProfile = skypeProfile;
		this.typeEvent = typeEvent.CREATION;
		this.commentEvent = commentEvent;
		this.itCorrespondantId = itCorrespondantId ;
		
	}



	public SkypeProfileDto getSkypeProfile() {
		return skypeProfile;
	}


	public void setSkypeProfile(SkypeProfileDto skypeProfile) {
		this.skypeProfile = skypeProfile;
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






	
	

}
