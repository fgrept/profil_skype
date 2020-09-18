package com.example.projetfilrouge.pskype.infrastructure;

import com.example.projetfilrouge.pskype.domain.collaborater.Collaborater;
import com.example.projetfilrouge.pskype.domain.collaborater.ICollaboraterDomain;
import com.example.projetfilrouge.pskype.domain.collaborater.OrganizationUnity;
import com.example.projetfilrouge.pskype.domain.collaborater.Site;
import com.example.projetfilrouge.pskype.domain.email.IMailDomain;
import com.example.projetfilrouge.pskype.domain.user.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashSet;
import java.util.Set;


import com.example.projetfilrouge.pskype.domain.exception.AllReadyExistException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfigForMail.class)
public class UserTest {
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo1 = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	private static final OrganizationUnity uo2 = new OrganizationUnity("SDI2", "I", "Big Data", site);
	private static final OrganizationUnity uo3 = new OrganizationUnity("SDI3", "I", "Outils interne", site);
	private static final Collaborater collab1 = new Collaborater("Doe", "John", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo1);
	private static final Collaborater collab2 = new Collaborater("McEnroe", "John", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
	private static final Collaborater collab3 = new Collaborater("King", "Stephen", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);
	private static final Collaborater collab4 = new Collaborater("McEnroe", "John", "112119", "01-43-34-45-57", "06-12-13-14-20", "john.tennis@gmail.com",uo2);

	@Autowired
	private IMailDomain mailDomain;
	
	@Autowired
	private IItCorrespondantDomain itCorrespondantDomain;
	
	@Autowired
	private ICollaboraterDomain collaboraterDomain;


	@Test
	@DisplayName("Vérifier la présence d'un CIL après sa création,"
			+ " lorsque le collaborateur existe déjà")
	public void verifyProfilAfterCreationWhenCollabNonExist () {
		collaboraterDomain.create(collab1);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles, "000000");
		
		assertThat(itCorrespondantDomain.findItCorrespondantByCollaboraterId(collab1.getCollaboraterId()).getCollaboraterId()).isEqualTo(collab1.getCollaboraterId());

	}
	
	
	@Test
	@DisplayName("Vérifier qu'il n'est pas possible de créer un CIL "
			+ "si il existe déjà sur ce collaborateur")
	public void verifyUnicityCILforACollab () {
		collaboraterDomain.create(collab1);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles, "000000");
		
		assertThatThrownBy(() -> {itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles, "000000");}).isInstanceOf(AllReadyExistException.class);

	}
	
	@Test
	//@Rollback(false)
	@DisplayName("Vérifier que la totalité des CIL peut être ramenée")
	public void verifyGetAllITCorresp () {
		
		int nbExisting = itCorrespondantDomain.findAllItCorrespondant().size();
		collaboraterDomain.create(collab1);
		collaboraterDomain.create(collab2);
		collaboraterDomain.create(collab3);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);		
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles, "000000");
		itCorrespondantDomain.createRoleCILtoCollab(collab2.getCollaboraterId(), roles, "000000");
		itCorrespondantDomain.createRoleCILtoCollab(collab3.getCollaboraterId(), roles, "000000");
		
		assertThat(itCorrespondantDomain.findAllItCorrespondant().size()).isEqualTo(nbExisting + 3);
	}
	
	@Test
	@DisplayName("Vérifier que le filtrage d'un profil fonctionne pour différents attribut")
	public void verifyCILPropertiesFiltration () {
		collaboraterDomain.create(collab1);
		collaboraterDomain.create(collab2);
		collaboraterDomain.create(collab3);
		collaboraterDomain.create(collab4);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);		
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles, "000000");
		itCorrespondantDomain.createRoleCILtoCollab(collab2.getCollaboraterId(), roles, "000000");
		itCorrespondantDomain.createRoleCILtoCollab(collab3.getCollaboraterId(), roles, "000000");
		itCorrespondantDomain.createRoleCILtoCollab(collab4.getCollaboraterId(), roles, "000000");
		
		assertAll(() -> assertThat(itCorrespondantDomain.
				findAllItCorrespondantFilters("112114", "Doe", "John", null, null, null))
				.hasSize(1)
				.allMatch(s -> s.getLastNamePerson() == "Doe"),
				
				() -> assertThat(itCorrespondantDomain.
						findAllItCorrespondantFilters(null, null, "John", null, null, null))
						.hasSize(3)
						.allMatch(s -> s.getFirstNamePerson() == "John"),
						
				() -> assertThat(itCorrespondantDomain.
						findAllItCorrespondantFilters(null, "McEnroe", "John", null, null, null))
						.hasSize(2)
						.allMatch(s -> s.getFirstNamePerson() == "John")
						.allMatch(s -> s.getLastNamePerson() == "McEnroe"),
						
				() -> assertThat(itCorrespondantDomain.
						findAllItCorrespondantFilters(null, null, null, "01-43-34-45-57", null, null))
						.hasSize(2)
						.allMatch(s -> s.getDeskPhoneNumber() == "01-43-34-45-57"),
						
				() -> assertThat(itCorrespondantDomain.
						findAllItCorrespondantFilters(null, null, null, "01-43-34-45-57", "06-12-13-14-16", null))
						.hasSize(1)
						.allMatch(s -> s.getDeskPhoneNumber() == "01-43-34-45-57")
						.allMatch(s -> s.getMobilePhoneNumber() == "06-12-13-14-16"),
						
				() -> assertThat(itCorrespondantDomain.
						findAllItCorrespondantFilters(null, null, null, null, null, "john.tennis@gmail.com"))
						.hasSize(2)
						.allMatch(s -> s.getMailAdress() == "john.tennis@gmail.com")
				);
	}


	

}
