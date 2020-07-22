package com.bnpparibas.projetfilrouge.pskype.exposition.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration du swagger
 * @author Judicaël
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bnpparibas.projetfilrouge.pskype.exposition"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()//
                .title("Swagger profil skype") //
                .description("Description profil skype") //
                .license("private use") //
                .licenseUrl("none") //
                .termsOfServiceUrl("") //
                .version("1.0") //
                .contact(new Contact("Judicael", "", "caludije@gmail.com")) //
                .build();
    }
}
