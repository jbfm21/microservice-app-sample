package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class OrderShippedEvent implements Serializable {
	private static final long serialVersionUID = -7088495552344017666L;
	private UUID orderId;
	private UUID paymentId;
	private UUID shippingId;
	
}
