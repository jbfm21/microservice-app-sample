package br.com.example.microservice.shopdomain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.example.microservice.shopdomain.model.CardDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder  @AllArgsConstructor
public class ValidatePaymentCommand {

	 @TargetAggregateIdentifier
	 private UUID paymentId;
	 private UUID orderId;
	 private CardDetailsDTO cardDetails;
}
