package br.com.example.microservice.shopdomain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class CancelOrderCommand 
{
	@TargetAggregateIdentifier
	private UUID orderId;

}