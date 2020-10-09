package com.example.projetfilrouge.pskype.security;


import javax.sql.DataSource;

import com.example.projetfilrouge.pskype.security.jwt.JwtAuthenticationEntryPoint;
import com.example.projetfilrouge.pskype.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	public static final int TOKEN_VALIDITY_SECONDS = 24*60*60; //1 jour
	
	@Autowired
	private UserDetailsService itCorrespondantUserDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(itCorrespondantUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	//méthode de portée public car associée à un bean
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	/**
	 * Méthode d'authorisation d'accès
	 * Les rôles sont gérés de façon plus fine via l'annotation @Secured
	 * sécurisation par cookie
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
	 * 3) Méthode non sécurisée
	 */
//	@Override
//    public void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();
//	}


	
	private PersistentTokenRepository persistentTokenRepository() {
		
		final JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}
	@Override
	protected void configure(final HttpSecurity httpSecurity) throws Exception {

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity
				.cors().and()
				.csrf().disable()
				// dont authenticate this authentication request
				.authorizeRequests().antMatchers("/authenticate").permitAll()
				// and authorize swagger-ui
				.antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
				// all other requests need to be authenticated
				.anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
						exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint) //
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token","Authorization","count"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token", "authorization","Authorization", "count"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
}
