package br.com.example.microservice.shopdomain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class OrderShippedEvent {
	private UUID orderId;
	private UUID paymentId;
	private UUID shippingId;
	
}
