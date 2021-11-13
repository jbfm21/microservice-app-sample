package br.com.example.microservice.order.domain.update;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.ProductCountDecrementedEvent;
import br.com.example.microservice.order.domain.event.ProductCountIncrementedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exceptions.OrderAlreadyConfirmedException;
import br.com.example.microservice.order.domain.update.command.DecrementProductCountCommand;
import br.com.example.microservice.order.domain.update.command.IncrementProductCountCommand;

public class OrderItem {

    @EntityId
    private final String productId;
    private Integer count;
    private boolean orderConfirmed;

    public OrderItem(String productId) {
        this.productId = productId;
        this.count = 1;
    }

    @CommandHandler
    public void handle(IncrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(command.getOrderId());
        }
        apply(ProductCountIncrementedEvent.builder().orderId(command.getOrderId()).productId(productId).build());
    }

    @CommandHandler
    public void handle(DecrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(command.getOrderId());
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
    }

    @EventSourcingHandler
    public void on(ProductCountDecrementedEvent event) {
        this.count--;
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderConfirmed = true;
    }

}