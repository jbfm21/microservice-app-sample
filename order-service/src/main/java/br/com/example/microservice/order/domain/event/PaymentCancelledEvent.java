package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import br.com.example.microservice.order.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class PaymentCancelledEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1596232534730183361L;
	private UUID paymentId;
	private UUID orderId;
	private PaymentStatus paymentStatus;
	
}
