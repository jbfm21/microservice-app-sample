package br.com.example.microservice.payment.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.example.microservice.payment.domain.CardDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder  @AllArgsConstructor
public class ValidatePaymentCommand {

	 @TargetAggregateIdentifier
	 private UUID paymentId;
	 private UUID orderId;
	 private CardDetails cardDetails;
}
