package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class OrderShippedEvent implements Serializable {
	private static final long serialVersionUID = -7088495552344017666L;
	private final UUID orderId;
	private final UUID paymentId;
	private final UUID shippingId;
	
}
