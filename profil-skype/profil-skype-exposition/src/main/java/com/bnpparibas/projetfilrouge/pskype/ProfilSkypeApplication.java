package com.bnpparibas.projetfilrouge.pskype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EnableJpaRepositories(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EntityScan("com.bnpparibas.projetfilrouge.pskype")
public class ProfilSkypeApplication {
	
	
	//IMPORTANT : 
	//Par défaut, la sécurité sera activée alors qu'il n'existe aucun user avec mot de passe en base.
	//Voici ce qu'il faut faire :
	//1) Lancer une 1ère fois l'application pour ajouter l'attribut password.
	//2) Mettre à jour en base le password et l'id annuaire d'un ItCorrespondant qui a le rôle ROLE_ADMIN
	//En cible, la colonne password sera créé en même temps que la table et un data.sql permettra d'initialiser l'admin.
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ProfilSkypeApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(ProfilSkypeApplication.class, args);
		LOG.info("Application is running!\n look at http://localhost:9095/swagger-ui.html");
	}
}
