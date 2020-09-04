package com.example.projetfilrouge.pskype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.projetfilrouge.pskype")
@EnableJpaRepositories
public class SpringBootApplicationTest {
	
	public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationTest.class, args);
    }
}