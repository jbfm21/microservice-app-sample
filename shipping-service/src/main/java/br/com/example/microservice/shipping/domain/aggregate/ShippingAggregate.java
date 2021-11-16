package br.com.example.microservice.shipping.domain.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.example.microservice.shipping.domain.ShippingStatus;
import br.com.example.microservice.shopdomain.command.ShipOrderCommand;
import br.com.example.microservice.shopdomain.event.OrderShippedEvent;
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
    private ShippingStatus shippingStatus;

    @CommandHandler
    public ShippingAggregate(ShipOrderCommand command){
    	AggregateLifecycle.apply(OrderShippedEvent.builder()
    											  .shippingId(command.getShippingId())
    											  .orderId(command.getOrderId())
    											  .paymentId(command.getPaymentId())
    											  .build());
    			
    }
    
    @EventSourcingHandler
    protected void on(OrderShippedEvent event){
        this.shippingId = event.getShippingId();
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
		//TODO: make this better
        this.shippingStatus = ShippingStatus.COMPLETED;
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
}
