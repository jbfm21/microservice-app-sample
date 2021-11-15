package br.com.example.microservice.payment.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class CreateInvoiceCommand 
{
	 @TargetAggregateIdentifier
	 public final UUID paymentId;

     public final UUID orderId;
}
