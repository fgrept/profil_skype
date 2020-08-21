package com.bnpparibas.projetfilrouge.pskype.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Classe de configuration de la sécurité
 * @author Judicaël
 *
 */
/*
 * Pour désactiver la sécurité, il faut :
 * 1) Désactiver la sécurité globale.
 * 2) Changer la méthode configure pour authoriser toutes les requêtes. 
 */
@Configuration
//1) @EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	public static final int TOKEN_VALIDITY_SECONDS = 24*60*60; //1 jour
	
	@Autowired
	private UserDetailsService itCorrespondantUserDetailsService;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(itCorrespondantUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	//méthode de portée public car associée à un bean
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	/**
	 * Méthode d'authorisation d'accès
	 * Les rôles sont gérés de façon plus fine via l'annotation @Secured
	 */
/*2)
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/*").permitAll()
		.antMatchers("/profile/**","/users/**", "/login","/logout","/collaborater/**","/events/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginProcessingUrl("/login")
		.permitAll()
		.and()
		.rememberMe()
		.key("secretKey")
		.rememberMeCookieName("remember-me-cookie")
		.tokenRepository(persistentTokenRepository())
		.tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
		;
	}
*/
	/**
	 * 3) On autorise toutes les requêtes
	 */
	@Override
    public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll();
	}
	
	private PersistentTokenRepository persistentTokenRepository() {
		
		final JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}

	
}
