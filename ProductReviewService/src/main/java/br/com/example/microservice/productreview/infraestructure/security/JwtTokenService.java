package br.com.example.microservice.productreview.infraestructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

	 public JwtAuthenticationToken getAuthentication() {
		 return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	 }
	 
	 public String getToken() 
	 {
		 return getAuthentication().getToken().getTokenValue();
	 }
}
