package br.com.example.microservice.shipping.domain.event;

import java.io.Serializable;
import java.util.UUID;

import br.com.example.microservice.shipping.domain.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder  @AllArgsConstructor
public class OrderShippedEvent implements Serializable 
{
	private static final long serialVersionUID = 5993522327095902110L;
	private final UUID shippingId;
	private final UUID orderId;
	private final UUID paymentId;
	private final ShippingStatus shipmentStatus;
}
