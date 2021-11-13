package br.com.example.microservice.order.domain.event;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

//https://stackoverflow.com/questions/68276141/jackson-although-at-least-one-creator-exists-no-string-argument-constructor
//https://projectlombok.org/features/experimental/Jacksonized
@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class OrderConfirmedEvent implements Serializable {
	
	private static final long serialVersionUID = 1228743830659327108L;
	private final String orderId;
}
