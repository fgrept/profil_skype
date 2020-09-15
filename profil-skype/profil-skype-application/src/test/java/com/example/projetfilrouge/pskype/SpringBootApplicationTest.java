package com.example.projetfilrouge.pskype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.projetfilrouge.pskype")

public class SpringBootApplicationTest {
	
	public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationTest.class, args);
    }
}