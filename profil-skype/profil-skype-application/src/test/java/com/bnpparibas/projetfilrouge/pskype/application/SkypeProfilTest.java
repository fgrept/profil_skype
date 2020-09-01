package com.bnpparibas.projetfilrouge.pskype.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfileEvent;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.TypeEventEnum;

@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = {SkypeProfileManagmentImpl.class})

/**
 * Dans cette classe de test, deux évènements sont identiques si le commentaire et le type sont identiques (cf méthode equals)
 *  
 * @author La Fabrique
 *
 */
public class SkypeProfilTest {
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	
	@Autowired
	private ISkypeProfileManagement skypeProfilManagement;
	
	@MockBean
	private ICollaboraterDomain collaboraterDomain;
	
	@MockBean
	private IItCorrespondantDomain itCorrespondantDomain;
	
	@MockBean
	private ISkypeProfileDomain skypeProfilDomain;
	
	@MockBean
	private ISkypeProfileEventDomain eventDomain;


	@Test
	@DisplayName("Vérifier que l'on crée un évènement quand on crée un profil")
	public void verifyEventWhenCreated () {

		//Given
		Collaborater collab = new Collaborater("McEnroe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		SkypeProfile skypeProfile = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		ItCorrespondant collabCil = new ItCorrespondant("Power", "Monsieur", "212114", "01-45-34-45-56", "07-12-13-14-15", "mr.power@gmail.com",uo);
		String eventComment = "c'est mon premier test";

		when(skypeProfilDomain.create(skypeProfile)).thenReturn(true);
		when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("212114")).thenReturn(collabCil);
						
		skypeProfilManagement.addNewSkypeProfile(skypeProfile, "212114", eventComment);
		
		// When
		SkypeProfileEvent eventCreation = new SkypeProfileEvent(eventComment, skypeProfile, collabCil, TypeEventEnum.CREATION);

		//Then
		Mockito.verify(eventDomain).create(eventCreation);
	}

	
	@Test
	@DisplayName("Vérifier règles sur la désactivation")
	public void verifyEventWhenDisabled () {
		
		//Given
		Collaborater collab = new Collaborater("McEnroe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		ItCorrespondant collabCil = new ItCorrespondant("Power", "Monsieur", "212114", "01-45-34-45-56", "07-12-13-14-15", "mr.power@gmail.com",uo);
		String eventComment = "c'est mon premier test";
		String comment = "Commentaire utilisateur : <<" + eventComment + ">>";

		SkypeProfile profileAvt = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		profileAvt.setStatusProfile(StatusSkypeProfileEnum.ENABLED);
		SkypeProfile profileAprès = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		profileAprès.setStatusProfile(StatusSkypeProfileEnum.DISABLED);

		SkypeProfileEvent eventAfter = new SkypeProfileEvent(comment, profileAprès, collabCil, TypeEventEnum.DESACTIVATION);
		
		when(skypeProfilDomain.findSkypeProfileByIdCollab("112114")).thenReturn(profileAvt);
		when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("212114")).thenReturn(collabCil);
		when(skypeProfilDomain.update(any(SkypeProfile.class))).thenReturn(true);
		
		//When		
		skypeProfilManagement.updateSkypeProfile(profileAprès, "212114", eventComment);		
		
		//Then
		Mockito.verify(eventDomain).create(eventAfter);
		Mockito.verify(skypeProfilDomain).update(argThat((SkypeProfile profil) -> profil.getExpirationDate().before(new Date())));

	}
	
	@Test
	@DisplayName("Vérifier règles sur l'activation")
	public void verifyEventWhenActivated () {
		
		//Given
		Collaborater collab = new Collaborater("McEnroe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		ItCorrespondant collabCil = new ItCorrespondant("Power", "Monsieur", "212114", "01-45-34-45-56", "07-12-13-14-15", "mr.power@gmail.com",uo);
		String eventComment = "c'est mon premier test";
		String comment = "Commentaire utilisateur : <<" + eventComment + ">>";

		SkypeProfile profileAvt = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		profileAvt.setStatusProfile(StatusSkypeProfileEnum.DISABLED);
		SkypeProfile profileAprès = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		profileAprès.setStatusProfile(StatusSkypeProfileEnum.ENABLED);

		SkypeProfileEvent eventAfter = new SkypeProfileEvent(comment, profileAprès, collabCil, TypeEventEnum.ACTIVATION);
		
		when(skypeProfilDomain.findSkypeProfileByIdCollab("112114")).thenReturn(profileAvt);
		when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("212114")).thenReturn(collabCil);
		when(skypeProfilDomain.update(any(SkypeProfile.class))).thenReturn(true);
		
		//When		
		skypeProfilManagement.updateSkypeProfile(profileAprès, "212114", eventComment);		
		
		//Then
		Mockito.verify(eventDomain).create(eventAfter);
		Mockito.verify(skypeProfilDomain).update(argThat((SkypeProfile profil) -> profil.getExpirationDate().compareTo(profileAprès.getExpirationDate()) == 0));

	}
	
	@Test
	@DisplayName("Vérifier règles sur la modification de 2 champs")
	public void verifyEventFieldWhenModified () {
		
		//Given
		Collaborater collab = new Collaborater("McEnroe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		ItCorrespondant collabCil = new ItCorrespondant("Power", "Monsieur", "212114", "01-45-34-45-56", "07-12-13-14-15", "mr.power@gmail.com",uo);
		String eventComment = "c'est mon premier test";
		String comment = "Commentaire utilisateur : <<c'est mon premier test>> - Champs modifiés : dialPlan : DP-IT, exUmEnabled : true";

		SkypeProfile profileAvt = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		SkypeProfile profileAprès = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-IT", "M002117014", true, "Linked Mailbox", "user", collab);

		SkypeProfileEvent eventAfter = new SkypeProfileEvent(comment, profileAprès, collabCil, TypeEventEnum.MODIFICATION);
		
		when(skypeProfilDomain.findSkypeProfileByIdCollab("112114")).thenReturn(profileAvt);
		when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("212114")).thenReturn(collabCil);
		when(skypeProfilDomain.update(any(SkypeProfile.class))).thenReturn(true);
		
		//When		
		skypeProfilManagement.updateSkypeProfile(profileAprès, "212114", eventComment);		
		
		//Then
		Mockito.verify(eventDomain).create(eventAfter);

	}
	
	@Test
	@DisplayName("Vérifier règles le changement de SIP")
	public void verifyEventWhenSIPChange () {
		
		//Given
		Collaborater collab = new Collaborater("McEnroe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		ItCorrespondant collabCil = new ItCorrespondant("Power", "Monsieur", "212114", "01-45-34-45-56", "07-12-13-14-15", "mr.power@gmail.com",uo);
		String eventComment = "c'est mon premier test";
		String comment = "Commentaire utilisateur : <<" + eventComment + ">>" + " - nouveau SIP : " + "sip:stefanieeee.radelle@live.bnpparibas.com";
		
		SkypeProfile profileAvt = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);
		SkypeProfile profileAprès = new SkypeProfile("sip:stefanieeee.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", "user", collab);

		SkypeProfileEvent eventAfter = new SkypeProfileEvent(comment, profileAprès, collabCil, TypeEventEnum.CREATION);
		
		when(skypeProfilDomain.findSkypeProfileByIdCollab("112114")).thenReturn(profileAvt);
		when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("212114")).thenReturn(collabCil);
		when(skypeProfilDomain.update(any(SkypeProfile.class))).thenReturn(true);
		
		//When		
		skypeProfilManagement.updateSkypeProfile(profileAprès, "212114", eventComment);		
		
		//Then
		Mockito.verify(eventDomain).create(eventAfter);

	}
	
}
