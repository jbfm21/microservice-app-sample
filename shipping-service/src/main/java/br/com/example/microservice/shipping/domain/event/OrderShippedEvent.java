package br.com.example.microservice.shipping.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class OrderShippedEvent implements Serializable 
{
	private static final long serialVersionUID = 5993522327095902110L;
	private final UUID shippingId;
	private final UUID orderId;
	private final UUID paymentId;
}
