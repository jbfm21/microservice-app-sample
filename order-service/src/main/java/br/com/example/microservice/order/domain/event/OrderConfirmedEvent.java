package br.com.example.microservice.order.domain.event;

import java.util.UUID;

import br.com.example.microservice.shopdomain.model.CardDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

//https://stackoverflow.com/questions/68276141/jackson-although-at-least-one-creator-exists-no-string-argument-constructor
//https://projectlombok.org/features/experimental/Jacksonized
@Jacksonized @Value @Builder  @AllArgsConstructor 
public class OrderConfirmedEvent {
	private UUID orderId;
	private CardDetailsDTO cardDetails;
}
