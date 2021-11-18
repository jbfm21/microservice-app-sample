package br.com.example.microservice.order.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class ProductCountIncrementedEvent{
	private UUID orderId;
	private UUID productId;
}
