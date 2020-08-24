package com.bnpparibas.projetfilrouge.pskype.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bnpparibas.projetfilrouge.pskype.domain.ICollaboraterDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.IItCorrespondantDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileEventDomain;
import com.bnpparibas.projetfilrouge.pskype.domain.ItCorrespondant;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.RoleTypeEnum;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;

@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = {ItCorrespondantManagementImpl.class})

/**
 * Dans cette classe de test, deux évènements sont identiques si le commentaire et le type sont identiques (cf méthode equals)
 *  
 * @author La Fabrique
 *
 */



public class UserTest {

	@MockBean
	private IItCorrespondantDomain itCorrespondantDomain;

	
	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IItCorrespondantManagment itCorrespondantManagment;	

	@MockBean
	private ISkypeProfileEventDomain eventDomain;
	
	private static final Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
	private static final OrganizationUnity uo = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
	
	
	@Test
	@DisplayName("Vérifier la création d'un USER CIL")
	public void verifyWhenNewCreation () {
		
		//Given
		ItCorrespondant itCorrespondantTest = new ItCorrespondant("Mehdi", "ELO", "479680", "01-02-02-10-10", "06-11-11-11-11", "mehdi.elo@gmail.com",uo);
		
		when(itCorrespondantDomain.createFull(itCorrespondantTest)).thenReturn(true);
		
		//When
		
		itCorrespondantManagment.createFullItCorrespondant(itCorrespondantTest) ;
								
		//Then
		
		Mockito.verify(itCorrespondantDomain).createFull(itCorrespondantTest);
				
		
	}
	
	
	@Test
	@DisplayName("Vérifier l'attribution d'un rôle à un CIL")
	public void verifyAttributionOfRole () {
		
		
		//Given
		ItCorrespondant itCorrespondantTest = new ItCorrespondant("Mehdi", "ELO", "479680", "01-02-02-10-10", "06-11-11-11-11", "mehdi.elo@gmail.com",uo);
		
		Set<RoleTypeEnum> roles = new HashSet<RoleTypeEnum>();
		roles.add(RoleTypeEnum.ROLE_USER);
		
		//roles.add(RoleTypeEnum.ROLE_RESP);
		
		when(itCorrespondantDomain.createFull(itCorrespondantTest)).thenReturn(true);
		
		when(itCorrespondantDomain.createRoleCILtoCollab("479680", roles,"0000")).thenReturn(true) ;
		
		//when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("479680").getRoles()).thenReturn(roles) ;
		
		//when(itCorrespondantDomain.findItCorrespondantByCollaboraterId("479680")).thenReturn(itCorrespondantTest) ;
		
		//When
		
		itCorrespondantManagment.createFullItCorrespondant(itCorrespondantTest);

	 	itCorrespondantTest.addRole(RoleTypeEnum.ROLE_USER);
		
		//Then
			
	 	
		Mockito.verify(itCorrespondantDomain).findItCorrespondantByCollaboraterId("479680").getRoles();
		
		 
		

	}

}
