package br.com.example.microservice.order.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class CreateShippingCommand {

	 @TargetAggregateIdentifier
	 public final UUID shippingId;

	 public final UUID orderId;

	 public final UUID paymentId;
}
