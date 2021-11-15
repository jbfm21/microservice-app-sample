package br.com.example.microservice.order.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder @AllArgsConstructor
public class ShipOrderCommand {

	 @TargetAggregateIdentifier
	 private UUID shippingId;

	 private UUID orderId;

	 private UUID paymentId;
}
