package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

//TODO: Not used 
@Jacksonized @Value @Builder  @AllArgsConstructor
public class InvoiceCreatedEvent implements Serializable {

	private static final long serialVersionUID = 5974296940925380384L;
	private UUID paymentId;
	private UUID orderId;
}
	