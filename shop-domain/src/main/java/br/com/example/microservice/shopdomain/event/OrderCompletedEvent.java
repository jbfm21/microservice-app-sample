package br.com.example.microservice.shopdomain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderCompletedEvent {
	
	private UUID orderId;
}
