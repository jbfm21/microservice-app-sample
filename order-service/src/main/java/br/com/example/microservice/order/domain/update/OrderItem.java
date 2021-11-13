package br.com.example.microservice.order.domain.update;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.ProductCountDecrementedEvent;
import br.com.example.microservice.order.domain.event.ProductCountIncrementedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exceptions.BusinessException;
import br.com.example.microservice.order.domain.update.command.DecrementProductCountCommand;
import br.com.example.microservice.order.domain.update.command.IncrementProductCountCommand;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
public class OrderItem {

    @EntityId
    private final UUID productId;
    private Long count;
    private boolean orderConfirmed;

    public OrderItem(UUID productId) {
        this.productId = productId;
        this.count = 1L;
    }

    @CommandHandler
    public void handle(IncrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new BusinessException.OrderAlreadyConfirmedException(command.getOrderId());
        }
        apply(ProductCountIncrementedEvent.builder().orderId(command.getOrderId()).productId(productId).build());
    }

    @CommandHandler
    public void handle(DecrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new BusinessException.OrderAlreadyConfirmedException(command.getOrderId());
        }

        if (count <= 1) {
        	apply(ProductRemovedEvent.builder().orderId(command.getOrderId()).productId(productId).build());
        } else {
        	apply(ProductCountDecrementedEvent.builder().orderId(command.getOrderId()).productId(productId).build());
        }
    }

    @EventSourcingHandler
    public void on(ProductCountIncrementedEvent event) {
        this.count++;
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }

    @EventSourcingHandler
    public void on(ProductCountDecrementedEvent event) {
        this.count--;
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderConfirmed = true;
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }

}