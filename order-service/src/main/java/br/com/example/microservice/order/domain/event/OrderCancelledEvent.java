package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import br.com.example.microservice.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderCancelledEvent implements Serializable {
	
	private static final long serialVersionUID = -7776688679672564956L;
	private UUID orderId;
	private OrderStatus orderStatus;
}
