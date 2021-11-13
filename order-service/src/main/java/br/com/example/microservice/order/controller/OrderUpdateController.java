package br.com.example.microservice.order.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.microservice.order.domain.update.command.AddProductCommand;
import br.com.example.microservice.order.domain.update.command.ConfirmOrderCommand;
import br.com.example.microservice.order.domain.update.command.CreateOrderCommand;
import br.com.example.microservice.order.domain.update.command.DecrementProductCountCommand;
import br.com.example.microservice.order.domain.update.command.IncrementProductCountCommand;
import br.com.example.microservice.order.domain.update.command.RemoveProductCommand;
import br.com.example.microservice.order.domain.update.command.ShipOrderCommand;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/orders")

public class OrderUpdateController {

    private final CommandGateway commandGateway; //Enviar comandos para o Command Bus do Axon, sendo responsável por fazer com que os comandos cheguem até os handlers do Agregado.
    private EventStore eventStore;

    public OrderUpdateController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }
    
    //Visão geral dos eventos que foram gerados para um agregad
    @GetMapping("/events/{aggregate-id}")
    @Transactional(readOnly = true)
    public List<EventMessage> listEvents(@PathVariable ("aggregate-id") String aggregateId) {
    	return eventStore.readEvents(aggregateId)
                .asStream()
                .collect(Collectors.toList());
    }    

   
    @PostMapping("/")
    public CompletableFuture<Void>  createOrder() {
    	return createOrder(UUID.randomUUID().toString());
    }

    @PostMapping("/{order-id}")
    public CompletableFuture<Void>  createOrder(@PathVariable("order-id") String orderId) {
    	CreateOrderCommand command = new CreateOrderCommand(orderId);
    	log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @PostMapping("/{order-id}/product/{product-id}")
    public CompletableFuture<Void> addProduct(@PathVariable("order-id") String orderId,
                                              @PathVariable("product-id") String productId) {
    	AddProductCommand command = new AddProductCommand(orderId, productId);
    	log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @PutMapping("/{order-id}/product/{product-id}/increment")
    public CompletableFuture<Void> incrementProduct(@PathVariable("order-id") String orderId,
                                                    @PathVariable("product-id") String productId) {
    	IncrementProductCountCommand command = new IncrementProductCountCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @PutMapping("/{order-id}/product/{product-id}/decrement")
    public CompletableFuture<Void> decrementProduct(@PathVariable("order-id") String orderId,
                                                    @PathVariable("product-id") String productId) {
    	DecrementProductCountCommand command = new DecrementProductCountCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }
    
    @DeleteMapping("/{order-id}/product/{product-id}")
    public CompletableFuture<Void> removeProduct(@PathVariable("order-id") String orderId,
            									@PathVariable("product-id") String productId) {
    	RemoveProductCommand command = new RemoveProductCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @PutMapping("/{order-id}/confirm")
    public CompletableFuture<Void> confirmOrder(@PathVariable("order-id") String orderId) {
    	ConfirmOrderCommand command = new ConfirmOrderCommand(orderId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @PutMapping("/{order-id}/ship")
    public CompletableFuture<Void> shipOrder(@PathVariable("order-id") String orderId) {
    	ShipOrderCommand command = new ShipOrderCommand(orderId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }
}