package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderCreatedEvent 
{
	private UUID orderId;
	private UUID userId;
}
