package br.com.example.microservice.productreview.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.example.microservice.productreview.infraestructure.security.KeycloakRealmRoleConverter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 http
	        .authorizeRequests()
	        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(KeycloakRealmRoleConverter.jwtAuthenticationConverter())));
	}	
	
	//Para permitir que circuitbreaker consiga acessar o contexto de seguranca na hora de chamar outro webservice
	@PostConstruct
	public void enableAuthenticationContextOnSpawnedThreads() {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}
}
