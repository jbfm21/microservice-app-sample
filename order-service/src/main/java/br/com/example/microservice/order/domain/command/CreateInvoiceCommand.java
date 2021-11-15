package br.com.example.microservice.order.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder  @AllArgsConstructor
public class CreateInvoiceCommand 
{
	 @TargetAggregateIdentifier
	 private UUID paymentId;

     private UUID orderId;
}
