package br.com.example.microservice.apigateway.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import reactor.core.publisher.Mono;

@RestController
public class SecurityInfoController {

	 @GetMapping(value = "/token")
	 public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
	     return Mono.just(authorizedClient.getAccessToken().getTokenValue());
	 }

	 @GetMapping("/")
	 public Mono<String> index(WebSession session) {
		 return Mono.just(session.getId());
	 }
	 
	 @RequestMapping("/greeting")
	    public String greeting(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
	        model.addAttribute("accessToken", client.getAccessToken());
	        return "greeting";
	    }
}
