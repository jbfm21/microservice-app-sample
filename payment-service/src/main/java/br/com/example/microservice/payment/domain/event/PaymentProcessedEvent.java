package br.com.example.microservice.payment.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class PaymentProcessedEvent implements Serializable {

	private static final long serialVersionUID = 8569672088869313664L;
	private UUID paymentId;
	private UUID orderId;
}
