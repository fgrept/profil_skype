package com.bnpparibas.projetfilrouge.pskype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EnableJpaRepositories(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EntityScan("com.bnpparibas.projetfilrouge.pskype")
public class ProfilSkypeApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProfilSkypeApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(ProfilSkypeApplication.class, args);
		LOG.info("Application is running!\n look at http://localhost:9095/swagger-ui.html");
	}
}
