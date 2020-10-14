package com.example.projetfilrouge.pskype.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashSet;
import java.util.Set;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.ICollaboraterDomain;
import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;
import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import com.example.projetfilrouge.pskype.domain.email.IMailDomain;
import com.example.projetfilrouge.pskype.domain.skypeprofile.ISkypeProfileDomain;
import com.example.projetfilrouge.pskype.domain.skypeprofile.SkypeProfile;
import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;
import com.example.projetfilrouge.pskype.domain.user.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import com.example.projetfilrouge.pskype.infrastructure.email.MailSenderProfile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.example.projetfilrouge.pskype.domain.exception.AllReadyExistException;
import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.ISkypeProfileEventRepository;
import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.ISkypeProfileRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfigForMail.class)

public class SkypeProfilTest {
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	
	@Autowired
	private ISkypeProfileDomain skypeProfilDomain;
	
	@Autowired
	private ICollaboraterDomain collaboraterDomain;
	
	@Autowired
	private IItCorrespondantDomain itCorrespondantDomain;
	
	// pour le vidage des profils avant les tests auto
	@Autowired
	private ISkypeProfileRepository repoSkypeProfil;
	@Autowired
	private ISkypeProfileEventRepository repoSkypeProfilEvent;

/*
	@BeforeAll
	@DisplayName("Remise à zéro des profils skype avant le début de ces tests")
	// ou alors basculer sur une base in-memory
	public static void cleanTables (@Autowired ISkypeProfileRepository repoSkypeProfil, @Autowired ISkypeProfileEventRepository repoSkypeProfilEvent) {
		repoSkypeProfilEvent.deleteAll();
		repoSkypeProfil.deleteAll();
	}
	*/
	@Test
	//@Rollback(false)
	@DisplayName("Vérifier la consultation possible d'un profil après sa création")
	public void verifyProfilAfterCreation () {
		Collaborater collab = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);
		
		assertThat(skypeProfilDomain.findSkypeProfileBySip("aaa-bbb@gmail.com").getCollaborater())
			.isEqualTo(collab);

	}

	@Test
	@DisplayName("Vérifier qu'il n'est pas possible de créer un 2ème profil skype"
			+ "pour le même collaborateur")
	public void verifyUnicityProfil () {
		Collaborater collab = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);
		
		SkypeProfile skypeProfilBis = new SkypeProfile("aaa-bbb@gmail.com", collab);
		
		assertThatThrownBy(() -> {skypeProfilDomain.create(skypeProfilBis);}).isInstanceOf(AllReadyExistException.class);
	}

	@Test
	//@Rollback(false)
	@DisplayName("Vérifier qu'un cil peut avoit un profil skype")
	public void verifyCilCanHaveProfil () {
		Collaborater collab = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);
		itCorrespondantDomain.createRoleCILtoCollab(collab.getCollaboraterId(), roles, "000000");
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
			
		assertThat(skypeProfilDomain.create(skypeProfil)).isTrue();
	}

	@Test
	@DisplayName("Vérifier qu'il n'est pas possible de créer un profil skype"
			+ "sur une SIP dejà affecté à un autre profil")
	public void verifyUnicitySIP () {
		Collaborater collab = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);

		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-56", "06-12-13-14-15", "john.tennis@gmail.com",uo);		
		SkypeProfile skypeProfilBis = new SkypeProfile("aaa-bbb@gmail.com", collab2);
		
		assertThatThrownBy(() -> {skypeProfilDomain.create(skypeProfilBis);}).isInstanceOf(AllReadyExistException.class);

	}

	@Test
	@DisplayName("Vérifier que la totalité des profils peut être ramenée")
	public void verifyGetAllProfils () {
		
		int cptExist = skypeProfilDomain.findAllSkypeProfile().size();
		
		OrganizationUnity uo2 = new OrganizationUnity("SDI2", "I", "Big Data", site);
		OrganizationUnity uo3 = new OrganizationUnity("SDI3", "I", "Outils interne", site);
		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);
		SkypeProfile skypeProfil1 = new SkypeProfile("sip.john.doe@gmail.com", collab1);
		SkypeProfile skypeProfil2 = new SkypeProfile("sip.john.tennis@gmail.com", collab2);
		SkypeProfile skypeProfil3 = new SkypeProfile("sip.stephen.horror@gmail.com", collab3);
		skypeProfilDomain.create(skypeProfil1);
		skypeProfilDomain.create(skypeProfil2);
		skypeProfilDomain.create(skypeProfil3);
		
		assertThat(skypeProfilDomain.findAllSkypeProfile().size()).isEqualTo(3+cptExist);
		
	}
	
//	@Test
//	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour une UO ou site")
//	public void verifyProfilUOFiltration () {
//		Site site2 = new Site("8812", "Valmy 3", "41 rue de Paris", "93100", "Montreuil");
//		OrganizationUnity uo2 = new OrganizationUnity("SDI2", "I", "Big Data", site);
//		OrganizationUnity uo3 = new OrganizationUnity("SDI3", "I", "Outils interne", site2);
//		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
//		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
//		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);
//		Collaborater collab4 = new Collaborater("McEnroe", "John", "112119", "01-43-34-45-57", "06-12-13-14-20", "john.tennis@gmail.com",uo2);
//
//		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4,StatusSkypeProfileEnum.ENABLED);
//
//		skypeProfilDomain.create(skypeProfil1);
//		skypeProfilDomain.create(skypeProfil2);
//		skypeProfilDomain.create(skypeProfil3);
//		skypeProfilDomain.create(skypeProfil4);
//
//		assertAll(
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(null, null, null, null, null, null, null, null,"8812", null, null, null, null))
//						.hasSize(1)
//						.allMatch(s -> s.getSIP() == "sip:fabian.radelle@live.bnpparibas.com"),
//
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(null, null, null, null, null, null, null,"SDI2",null, null, null, null, null))
//						.hasSize(2)
//						.allMatch(s -> s.getCollaborater().getOrgaUnit().getOrgaUnityCode() == "SDI2")
//				);
//
//	}
	
//	@Test
//	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour différents attributs")
//	public void verifyProfilPropertiesFiltration () {
//		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
//		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo);
//		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo);
//		Collaborater collab4 = new Collaborater("Christ", "Jesus", "118119", "01-43-34-45-58", "06-12-13-14-17", "jesus.auciel@gmail.com",uo);
//
//		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4,StatusSkypeProfileEnum.ENABLED);
//
//		skypeProfilDomain.create(skypeProfil1);
//		skypeProfilDomain.create(skypeProfil2);
//		skypeProfilDomain.create(skypeProfil3);
//		skypeProfilDomain.create(skypeProfil4);
//
//		assertAll(
//				() -> assertThat(skypeProfilDomain.
//				findAllSkypeProfileFilters(false, null, null, null, null, null, null,null,null, null, null))
//				.hasSize(1)
//				.allMatch(s -> s.isEnterpriseVoiceEnabled() == false)
//				.allMatch(s -> s.getSIP() == "sip:stefan.radelle@live.bnpparibas.com"),
//
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(true, "InternationalNonAuthorized", null, null, null, null, null,null,null, null, null))
//						.hasSize(2)
//						.allMatch(s -> s.isEnterpriseVoiceEnabled() == true)
//						.allMatch(s -> s.getVoicePolicy() == "InternationalNonAuthorized"),
//
////				() -> assertThat(skypeProfilDomain.
////						findAllSkypeProfileFilters(null, "InternationalNonAuthorized", "DP-FR", null, null, null, null,null,null, null, null))
////						.hasSize(1)
////						.allMatch(s -> s.getVoicePolicy() == "InternationalNonAuthorized")
////						.allMatch(s -> s.getDialPlan() == "DP-FR"),
//
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(null, null, null, "M002117016", null, null, null,null,null, null, null))
//						.hasSize(2)
//						.allMatch(s -> s.getSamAccountName() == "M002117016"),
//
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(null, null, null, "M002117016", true, null, null,null,null, null, null))
//						.hasSize(1)
//						.allMatch(s -> s.getSamAccountName() == "M002117016")
//						.allMatch(s -> s.getSIP() == "sip:fabian.radelle@live.bnpparibas.com"));
//
////				() -> assertThat(skypeProfilDomain.
////						findAllSkypeProfileFilters(null, null, null, null, null, "Linked Google", null,null,null, null, null))
////						.hasSize(1)
////						.allMatch(s -> s.getExchUser() == "Linked Google")
////						.allMatch(s -> s.getSIP() == "sip:paulo.radelle@live.bnpparibas.com")
////				);
//
//	}

//	@Test
//	@DisplayName("Vérifier que le filtrage d'un profil fonctionne sur le statut (après un maj donc)")
//	public void verifyStatusFiltration () {
//		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
//		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo);
//		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo);
//		Collaborater collab4 = new Collaborater("Christ", "Jesus", "118119", "01-43-34-45-58", "06-12-13-14-17", "jesus.auciel@gmail.com",uo);
//
//		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3,StatusSkypeProfileEnum.ENABLED);
//		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4,StatusSkypeProfileEnum.ENABLED);
//
//		skypeProfilDomain.create(skypeProfil1);
//		skypeProfilDomain.create(skypeProfil2);
//		skypeProfilDomain.create(skypeProfil3);
//		skypeProfilDomain.create(skypeProfil4);
//
//		skypeProfil1.setStatusProfile(StatusSkypeProfileEnum.DISABLED);
//		skypeProfilDomain.update(skypeProfil1);
//
//		assertAll(
//				() -> assertThat(skypeProfilDomain.
//						findAllSkypeProfileFilters(null, null, null, null, null, "Linked Mailbox", StatusSkypeProfileEnum.DISABLED,null,null, null, null, null, null))
//						.hasSize(1)
//						.allMatch(s -> s.getSIP() == "sip:stefan.radelle@live.bnpparibas.com")
//				);
//
//	}
	
	@Test
	@DisplayName("Vérifier la suppresion d'un profil")
	public void verifyProfilDelete () {
		Collaborater collab = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);
		
		skypeProfilDomain.delete("aaa-bbb@gmail.com");
		
		assertThat(skypeProfilDomain.findSkypeProfileBySip("aaa-bbb@gmail.com")).isNull();

 	}

	@Test
	//@Rollback(false)
	@DisplayName("Vérifier l'authenticité d'un profil suite à consultation")
	public void verifyProfilAuthenticity() {
		Collaborater collab = new Collaborater("Doe", "John", "112115", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
	
		skypeProfilDomain.create(skypeProfil);
		
		SkypeProfile skypeProfilConsulted = skypeProfilDomain.consultSkypeProfile("aaa-bbb@gmail.com",StatusSkypeProfileEnum.ENABLED)  ;
	
		//assertThat(skypeProfilDomain.consultSkypeProfile("aaa-bbb@gmail.com",StatusSkypeProfileEnum.ENABLED)).isEqualTo(skypeProfil);
				
		assertAll(
				() -> assertThat(skypeProfilConsulted.getDialPlan()).isEqualTo(skypeProfil.getDialPlan()),
				
				() -> assertThat(skypeProfilConsulted.getExchUser()).isEqualTo(skypeProfil.getExchUser()),
				
				() -> assertThat(skypeProfilConsulted.getObjectClass()).isEqualTo(skypeProfil.getObjectClass()),
	
				() -> assertThat(skypeProfilConsulted.getSamAccountName()).isEqualTo(skypeProfil.getSamAccountName()),
				
				() -> assertThat(skypeProfilConsulted.getSIP()).isEqualTo(skypeProfil.getSIP()),
	
				() -> assertThat(skypeProfilConsulted.getVoicePolicy()).isEqualTo(skypeProfil.getVoicePolicy()),
	
				() -> assertThat(skypeProfilConsulted.getCollaborater()).isEqualTo(skypeProfil.getCollaborater())
				
				);
				
				
						
						
	}

}
