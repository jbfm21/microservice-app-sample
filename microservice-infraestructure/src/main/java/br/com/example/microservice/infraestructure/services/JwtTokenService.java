package br.com.example.microservice.infraestructure.services;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
		 Jwt token = getAuthentication().getToken();
		 return UUID.fromString(token.getSubject());
	 }
	 
	 public String getGivenName() {
		 Jwt token = getAuthentication().getToken();
		 return token.getClaimAsString("given_name");
	 }
	 public String getFamilyName() {
		 Jwt token = getAuthentication().getToken();
		 return token.getClaimAsString("family_name");
	 }
	 
	 public String getEmail() {
		 Jwt token = getAuthentication().getToken();
		 return token.getClaimAsString("email");
	 }
}
