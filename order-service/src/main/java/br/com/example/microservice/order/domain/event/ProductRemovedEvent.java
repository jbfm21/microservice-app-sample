package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class ProductRemovedEvent implements Serializable{
	private static final long serialVersionUID = -6534183385618816610L;
	private UUID orderId;
	private UUID productId;
}
