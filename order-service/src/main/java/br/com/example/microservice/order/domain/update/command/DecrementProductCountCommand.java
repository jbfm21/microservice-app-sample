package br.com.example.microservice.order.domain.update.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class DecrementProductCountCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final String productId;
}
