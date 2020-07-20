package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
	
	//@Autowired
	//private TestEntityManager entityManager;
	
	@Test
	//@Rollback(false)
	@DisplayName("Vérifier la consultation possible d'un profil après sa création")
	public void verifyProfilAfterCreation () {
		Collaborater collab = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
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
		Collaborater collab = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
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
		Collaborater collab = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);

		Collaborater collab2 = new Collaborater("John", "McEnroe", "112115", "01-43-34-45-56", "06-12-13-14-15", "john.tennis@gmail.com",uo);		
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
		Collaborater collab1 = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);
		Collaborater collab2 = new Collaborater("John", "McEnroe", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
		Collaborater collab3 = new Collaborater("Stephen", "King", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);
		SkypeProfile skypeProfil1 = new SkypeProfile("sip.john.doe@gmail.com", collab1);
		SkypeProfile skypeProfil2 = new SkypeProfile("sip.john.tennis@gmail.com", collab2);
		SkypeProfile skypeProfil3 = new SkypeProfile("sip.stephen.horror@gmail.com", collab3);
		skypeProfilDomain.create(skypeProfil1);
		skypeProfilDomain.create(skypeProfil2);
		skypeProfilDomain.create(skypeProfil3);
		
		assertThat(skypeProfilDomain.findAllSkypeProfile().size()).isEqualTo(3);
		
	}
	
	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour une UO")
	public void verifyProfilUOFiltration () {
		
		//assertThat(skypeProfilDomain.findSkypeProfileFilters());
		
	}
	
	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour différents attribut")
	public void verifyProfilPropertiesFiltration () {
		//use soft assertions
	}
	
	@Test
	@DisplayName("Vérifier qu'il est possible de cumuler au moins 3 filtres successifs"
			+ "sur une même recherche")
	public void verifyProfilCumulativeFiltration () {
		//use soft assertions
	}
	
	@Test
	@DisplayName("Vérifier qu'un évènement de création est bien crée lors"
			+ "de la création d'un profil")
	public void verifyEventAfterCreation () {
		
	}
	
	@Test
	@DisplayName("Vérifier qu'un évènement de mise à jour est bien crée lors"
			+ "de la mise à jour d'un profil")
	public void verifyEventAfterMoodification () {
		
	}
	

}
