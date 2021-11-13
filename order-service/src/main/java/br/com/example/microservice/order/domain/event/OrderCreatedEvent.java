package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.Map;

import org.axonframework.modelling.command.AggregateMember;

import br.com.example.microservice.order.domain.update.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class OrderCreatedEvent  implements Serializable 
{
	//Attention: this event need to hold all properties from aggregate, because the framework use this event to create a new aggregate and to load an exisit aggregate
	
	private static final long serialVersionUID = -684285158168364801L;

	private final String orderId;
}
