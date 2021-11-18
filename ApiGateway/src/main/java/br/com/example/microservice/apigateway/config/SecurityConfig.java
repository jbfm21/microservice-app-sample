package br.com.example.microservice.apigateway.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	 @Bean
	 public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
	      return http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
	                 .oauth2Login(withDefaults())
	                 .csrf().disable()
	                 .build();
	 }
  
}