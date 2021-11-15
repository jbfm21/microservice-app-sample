package br.com.example.microservice.payment.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


//TODO: Not user
@Jacksonized @Value @Builder @AllArgsConstructor
public class PaymentCreatedEvent implements Serializable {

	private static final long serialVersionUID = 6919187829506737281L;
	private UUID paymentId;
	private UUID orderId;
}
