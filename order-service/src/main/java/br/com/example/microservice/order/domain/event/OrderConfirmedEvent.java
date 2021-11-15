package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

//https://stackoverflow.com/questions/68276141/jackson-although-at-least-one-creator-exists-no-string-argument-constructor
//https://projectlombok.org/features/experimental/Jacksonized
@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderConfirmedEvent implements Serializable {
	
	private static final long serialVersionUID = 1228743830659327108L;
	private UUID orderId;
}
