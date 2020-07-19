package com.bnpparibas.projetfilrouge.pskype.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;


public class SkypeProfilTest {
	
	@Test
	@DisplayName("Contrôle sur la date d'expiration à la création du profil")
	public void expirationDateControlWhenCreated () {
		SkypeProfile profil = new SkypeProfile();
		
	    LocalDate deb = LocalDate.now();
	    LocalDate fin = profil.getExpirationDate().toInstant()
	    	      .atZone(ZoneId.systemDefault())
	    	      .toLocalDate();

		assertThat(Period.between(deb, fin).minusYears(2).isZero()).isTrue();
		
	}
}
