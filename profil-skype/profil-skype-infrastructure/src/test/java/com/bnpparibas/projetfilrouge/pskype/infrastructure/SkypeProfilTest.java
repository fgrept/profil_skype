package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
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
		Site site = new Site("8802", "Valmy 2", "41 rue de Valmy", "93100", "Montreuil");
		OrganizationUnity uo = new OrganizationUnity("SDI1", "I", "Business intelligence", site);
		Collaborater collab = new Collaborater("John", "Doe", "112114", "01-43-34-45-56", "06-12-13-14-15", "john.doe@gmail.com",uo);		
		collaboraterDomain.create(collab);
		
		SkypeProfile skypeProfil = new SkypeProfile("aaa-bbb@gmail.com", collab);
		skypeProfilDomain.create(skypeProfil);
		
		assertThat(skypeProfilDomain.findSkypeProfileBySip("aaa-bbb@gmail.com").getCollaborater())
			.isEqualTo(collab);

	}
}
