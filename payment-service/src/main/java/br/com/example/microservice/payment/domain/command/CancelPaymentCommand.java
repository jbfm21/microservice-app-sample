package br.com.example.microservice.payment.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.example.microservice.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class CancelPaymentCommand {

	@TargetAggregateIdentifier
	private UUID paymentId;
	private UUID orderId;
	private PaymentStatus paymentStatus = PaymentStatus.CANCELLED;
	
}
