package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
	
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.domain.SkypeProfile;
import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SkypeProfilTest {
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	
	@Autowired
	private ISkypeProfileDomain skypeProfilDomain;
	
	@Autowired
	private ICollaboraterDomain collaboraterDomain;
	
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
		
		assertThatThrownBy(() -> {
			skypeProfilDomain.create(skypeProfilBis);
		}).isInstanceOf(RuntimeException.class);
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
		
		assertThatThrownBy(() -> {
			skypeProfilDomain.create(skypeProfilBis);
		}).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Vérifier que la totalité des profils peut être ramenée")
	public void verifyGetAllProfils () {
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
		
		assertThat(skypeProfilDomain.findAllSkypeProfile().size()).isEqualTo(3);
		
	}
	
	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour une UO ou site")
	public void verifyProfilUOFiltration () {
		Site site2 = new Site("8812", "Valmy 3", "41 rue de Paris", "93100", "Montreuil");
		OrganizationUnity uo2 = new OrganizationUnity("SDI2", "I", "Big Data", site);
		OrganizationUnity uo3 = new OrganizationUnity("SDI3", "I", "Outils interne", site2);
		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);
		Collaborater collab4 = new Collaborater("McEnroe", "John", "112119", "01-43-34-45-57", "06-12-13-14-20", "john.tennis@gmail.com",uo2);

		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1);
		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2);
		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3);
		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4);

		skypeProfilDomain.create(skypeProfil1);
		skypeProfilDomain.create(skypeProfil2);
		skypeProfilDomain.create(skypeProfil3);
		skypeProfilDomain.create(skypeProfil4);		
				
		assertAll(						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, null, null, null, null, null,"8812"))
						.hasSize(1)
						.allMatch(s -> s.getSIP() == "sip:fabian.radelle@live.bnpparibas.com"),
						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, null, null, null, null,"SDI2",null))
						.hasSize(2)
						.allMatch(s -> s.getCollaborater().getOrgaUnit().getOrgaUnityCode() == "SDI2")
				);
		
	}	
	
	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour différents attributs")
	public void verifyProfilPropertiesFiltration () {
		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo);
		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo);
		Collaborater collab4 = new Collaborater("Christ", "Jesus", "118119", "01-43-34-45-58", "06-12-13-14-17", "jesus.auciel@gmail.com",uo);
		
		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1);
		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2);
		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3);
		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4);

		skypeProfilDomain.create(skypeProfil1);
		skypeProfilDomain.create(skypeProfil2);
		skypeProfilDomain.create(skypeProfil3);
		skypeProfilDomain.create(skypeProfil4);		
		
		assertAll(() -> assertThat(skypeProfilDomain.
				findAllSkypeProfileFilters(false, null, null, null, null, null, null,null,null))
				.hasSize(1)
				.allMatch(s -> s.isEnterpriseVoiceEnabled() == false)
				.allMatch(s -> s.getSIP() == "sip:stefan.radelle@live.bnpparibas.com"),
				
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(true, "InternationalNonAuthorized", null, null, null, null, null,null,null))
						.hasSize(2)
						.allMatch(s -> s.isEnterpriseVoiceEnabled() == true)
						.allMatch(s -> s.getVoicePolicy() == "InternationalNonAuthorized"),
						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, "InternationalNonAuthorized", "DP-FR", null, null, null, null,null,null))
						.hasSize(1)
						.allMatch(s -> s.getVoicePolicy() == "InternationalNonAuthorized")
						.allMatch(s -> s.getDialPlan() == "DP-FR"),
						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, "M002117016", null, null, null,null,null))
						.hasSize(2)
						.allMatch(s -> s.getSamAccountName() == "M002117016"),
						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, "M002117016", true, null, null,null,null))
						.hasSize(1)
						.allMatch(s -> s.getSamAccountName() == "M002117016")
						.allMatch(s -> s.getSIP() == "sip:fabian.radelle@live.bnpparibas.com"),
						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, null, null, "Linked Google", null,null,null))
						.hasSize(1)
						.allMatch(s -> s.getExchUser() == "Linked Google")
						.allMatch(s -> s.getSIP() == "sip:paulo.radelle@live.bnpparibas.com")
				);
		
	}	

	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne sur le statut (après un maj donc)")
	public void verifyStatusFiltration () {
		Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo);
		Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo);
		Collaborater collab4 = new Collaborater("Christ", "Jesus", "118119", "01-43-34-45-58", "06-12-13-14-17", "jesus.auciel@gmail.com",uo);
		
		SkypeProfile skypeProfil1 = new SkypeProfile("sip:stefan.radelle@live.bnpparibas.com", false, "InternationalNonAuthorized", "DP-FR", "M002117014", false, "Linked Mailbox", null, collab1);
		SkypeProfile skypeProfil2 = new SkypeProfile("sip:paulo.radelle@live.bnpparibas.com", true, "InternationalAuthorized", "DP-IT", "M002117015", false, "Linked Google", null, collab2);
		SkypeProfile skypeProfil3 = new SkypeProfile("sip:fabian.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-IT", "M002117016", true, null, null, collab3);
		SkypeProfile skypeProfil4 = new SkypeProfile("sip:anabella.radelle@live.bnpparibas.com", true, "InternationalNonAuthorized", "DP-US", "M002117016", false, "Linked Mailbox", null, collab4);

		skypeProfilDomain.create(skypeProfil1);
		skypeProfilDomain.create(skypeProfil2);
		skypeProfilDomain.create(skypeProfil3);
		skypeProfilDomain.create(skypeProfil4);		
		
		//TODO : en attente de update
		
		assertAll(						
				() -> assertThat(skypeProfilDomain.
						findAllSkypeProfileFilters(null, null, null, null, null, "Linked Mailbox", StatusSkypeProfileEnum.EXPIRED,null,null))
						.hasSize(1)
						.allMatch(s -> s.getSIP() == "sip:stefan.radelle@live.bnpparibas.com")
				);
		
	}	
	
}
