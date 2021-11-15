package br.com.example.microservice.payment.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class CreatePaymentCommand 
{
	@TargetAggregateIdentifier
	private UUID paymentId;
    private UUID orderId;
}
