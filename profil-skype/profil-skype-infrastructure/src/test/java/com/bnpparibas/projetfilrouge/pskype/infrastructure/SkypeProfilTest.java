package com.bnpparibas.projetfilrouge.pskype.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bnpparibas.projetfilrouge.pskype.domain.ISkypeProfileDomain;

@DataJpaTest
public class SkypeProfilTest {
	
	@Autowired
	private ISkypeProfileDomain skypeProfilDomain;
	
	@Test
	@DisplayName("Vérifier la consultation possible d'un profil après sa création")
	public void verifyProfilAfterCreation () {
		
	}
}
