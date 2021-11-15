package br.com.example.microservice.order.domain.command;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.example.microservice.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder @AllArgsConstructor
public class CancelOrderCommand 
{
	@TargetAggregateIdentifier
	private UUID orderId;
	private OrderStatus orderStatus = OrderStatus.CANCELLED;	
}