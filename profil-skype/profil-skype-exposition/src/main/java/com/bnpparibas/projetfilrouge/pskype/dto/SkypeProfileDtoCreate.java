package com.bnpparibas.projetfilrouge.pskype.dto;

import java.util.Date;

import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

/**
 * Classe exposée à l'utilisateur sur le profil Skype pour toute action de création, mise à jour
 * @author La Fabrique
 *
 */
public class SkypeProfileDtoCreate extends SkypeProfileDto{
	
	//toute action sur un profil skype est réalisée par un CIL et sera tracée en base évènement
	@Size(max = 17)
	private String itCorrespondantId;
	private String eventComment;
	
	
	public SkypeProfileDtoCreate() {
		
	}
	
	public SkypeProfileDtoCreate(String SIP, String collaboraterId, String itCorrespondantId) {

		this.itCorrespondantId=itCorrespondantId;
	}
	
	public SkypeProfileDtoCreate(String sIP, boolean enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, boolean exUmEnabled, String exchUser, String objectClass,
			@Size(max = 17) String collaboraterId, @Size(max = 17) String itCorrespondantId,
			StatusSkypeProfileEnum statusProfile, String eventComment) {
		super(sIP, enterpriseVoiceEnabled, voicePolicy, dialPlan,
				samAccountName, exUmEnabled, exchUser, objectClass,
				collaboraterId, statusProfile);

		this.itCorrespondantId = itCorrespondantId;
		this.eventComment = eventComment;
	}

	public String getItCorrespondantId() {
		return itCorrespondantId;
	}
	public void setItCorrespondantId(String itCorrespondantId) {
		this.itCorrespondantId = itCorrespondantId;
	}

	public String getEventComment() {
		return eventComment;
	}

	public void setEventComment(String eventComment) {
		this.eventComment = eventComment;
	}
	
}
