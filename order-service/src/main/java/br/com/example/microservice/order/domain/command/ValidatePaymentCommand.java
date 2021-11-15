package br.com.example.microservice.order.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.example.microservice.order.client.user.CardDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder  @AllArgsConstructor
public class ValidatePaymentCommand {

	 @TargetAggregateIdentifier
	 private UUID paymentId;
	 private UUID orderId;
	 private CardDetailsDTO cardDetails;
}
