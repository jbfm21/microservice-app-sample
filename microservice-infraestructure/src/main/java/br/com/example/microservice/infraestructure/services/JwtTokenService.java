package br.com.example.microservice.infraestructure.services;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

	 public JwtAuthenticationToken getAuthentication() {
		 return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	 }
	 
	 public String getToken() 
	 {
		 return getAuthentication().getToken().getTokenValue();
	 }
	 
	 public UUID getUserId()
	 {
		 return UUID.fromString(getAuthentication().getToken().getSubject());
	 }
	 
	 public String getGivenName() {
		 return getAuthentication().getToken().getClaimAsString("given_name");
	 }
	 public String getFamilyName() {
		 return getAuthentication().getToken().getClaimAsString("family_name");
	 }
	 
	 public String getEmail() {
		 return  getAuthentication().getToken().getClaimAsString("email");
	 }
}
