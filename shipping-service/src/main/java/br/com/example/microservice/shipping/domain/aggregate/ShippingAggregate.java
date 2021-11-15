package br.com.example.microservice.shipping.domain.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.example.microservice.shipping.domain.command.CreateShippingCommand;
import br.com.example.microservice.shipping.domain.event.OrderShippedEvent;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Aggregate
@Log4j2
@ToString @NoArgsConstructor
public class ShippingAggregate {

	@AggregateIdentifier
	private UUID shippingId;

	private UUID orderId;
	
    private UUID paymentId;

    @CommandHandler
    public ShippingAggregate(CreateShippingCommand command){
    	AggregateLifecycle.apply(new OrderShippedEvent(command.shippingId, command.orderId, command.paymentId));
    }
    
    @EventSourcingHandler
    protected void on(OrderShippedEvent event){
        this.shippingId = event.getShippingId();
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
        
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
}
