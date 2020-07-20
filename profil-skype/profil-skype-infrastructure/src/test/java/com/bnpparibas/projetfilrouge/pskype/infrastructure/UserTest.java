package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;

import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo1 = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	private static final OrganizationUnity uo2 = new OrganizationUnity("SDI2", "I", "Big Data", site);
	private static final OrganizationUnity uo3 = new OrganizationUnity("SDI3", "I", "Outils interne", site);
	private static final Collaborater collab1 = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo1);
	private static final Collaborater collab2 = new Collaborater("John", "McEnroe", "112115", "01-43-34-45-57", "06-12-13-14-16", "john.tennis@gmail.com",uo2);
	private static final Collaborater collab3 = new Collaborater("Stephen", "King", "118116", "01-43-34-45-58", "06-12-13-14-17", "stephen.horror@gmail.com",uo3);

	
	@Autowired
	private IItCorrespondantDomain itCorrespondantDomain;
	
	@Test
	@DisplayName("Vérifier la présence d'un CIL après sa création,"
			+ " lorsque le collaborateur n'existe pas")
	public void verifyProfilAfterCreationWhenCollabExist () {
		ItCorrespondant itCorrespondant0 = new ItCorrespondant("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com");
		
		itCorrespondantDomain.createFull(itCorrespondant0);		
		assertThat(itCorrespondantDomain.findItCorrespondantByCollaboraterId("112114")).isEqualTo(itCorrespondant0);
	}
	
	@Test
	@DisplayName("Vérifier la présence d'un CIL après sa création,"
			+ " lorsque le collaborateur existe déjà")
	public void verifyProfilAfterCreationWhenCollabNonExist () {
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);
		
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles);	
		assertThat(itCorrespondantDomain.findItCorrespondantByCollaboraterId("112114").getCollaboraterId()).isEqualTo(collab1.getCollaboraterId());
	}
	
	
	@Test
	@DisplayName("Vérifier que la totalité des CIL peut être ramenée")
	public void verifyGetAllITCorresp () {
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		roles.add(RoleTypeEnum.ROLE_RESP);
		
		itCorrespondantDomain.createRoleCILtoCollab(collab1.getCollaboraterId(), roles);
		itCorrespondantDomain.createRoleCILtoCollab(collab2.getCollaboraterId(), roles);
		itCorrespondantDomain.createRoleCILtoCollab(collab3.getCollaboraterId(), roles);
		
		assertThat(itCorrespondantDomain.findAllItCorrespondant().size()).isEqualTo(3);
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
	
	
}
