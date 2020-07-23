package com.bnpparibas.projetfilrouge.pskype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.bnpparibas.projetfilrouge.pskype")
@EnableJpaRepositories
public class SpringBootApplicationTest {
	
	public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationTest.class, args);
    }
}