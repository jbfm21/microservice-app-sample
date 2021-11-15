package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import br.com.example.microservice.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderCompletedEvent implements Serializable {
	
	private static final long serialVersionUID = -6497739059298695012L;
	private UUID orderId;
	private OrderStatus orderStatus;
}
