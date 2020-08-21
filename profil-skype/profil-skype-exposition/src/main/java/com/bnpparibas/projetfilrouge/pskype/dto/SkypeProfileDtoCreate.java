package com.bnpparibas.projetfilrouge.pskype.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

/**
 * Classe exposée à l'utilisateur sur le profil Skype pour toute action de création, mise à jour
 * @author La Fabrique
 *
 */
public class SkypeProfileDtoCreate extends SkypeProfileDto{
	
	//toute action sur un profil skype est réalisée par un CIL et sera tracée en base évènement
	@Size(min = 1, max = 17, message = "l'identifiant de l'utilisateur doit être compris entre 1 et  17 caractères")
	@NotNull
	private String itCorrespondantId;
	//contrôle pour éviter les injections html
	@Pattern(regexp = "^([A-Za-z1-9\\u00e0-\\u00f6\\u00f8-\\u00ff\\s,.'-]*)$",
			message ="le commentaire doit comprendre des caractères latins alphanumériques, ainsi que les caractères suivants ,.-'")
	private String eventComment;
	
	
	public SkypeProfileDtoCreate() {
		
	}
	
	public SkypeProfileDtoCreate(String SIP, String collaboraterId, String itCorrespondantId) {

		this.itCorrespondantId=itCorrespondantId;
	}
	
	public SkypeProfileDtoCreate(String sIP, String enterpriseVoiceEnabled, String voicePolicy, String dialPlan,
			String samAccountName, String exUmEnabled, String exchUser, String objectClass,
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
