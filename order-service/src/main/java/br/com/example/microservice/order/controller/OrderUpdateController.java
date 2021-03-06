package br.com.example.microservice.order.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.microservice.order.domain.command.AddProductCommand;
import br.com.example.microservice.order.domain.command.ConfirmOrderCommand;
import br.com.example.microservice.order.domain.command.CreateOrderCommand;
import br.com.example.microservice.order.domain.command.DecrementProductCountCommand;
import br.com.example.microservice.order.domain.command.IncrementProductCountCommand;
import br.com.example.microservice.order.domain.command.RemoveProductCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/orders")

public class OrderUpdateController {

    private final CommandGateway commandGateway;  
    private EventStore eventStore; 

    @Autowired
    public OrderUpdateController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }
    
    @Operation(summary = "List all events by aggreate id stored in event store")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one event", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = EventMessage.class)) })
    })
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_EVENTS_FINDALL')")
    @GetMapping("/events/{aggregate-id}")
    @Transactional(readOnly = true)
    public List<EventMessage> listEvents(@PathVariable ("aggregate-id") String aggregateId) {
    	return eventStore.readEvents(aggregateId)
                .asStream()
                .collect(Collectors.toList());
    }    

   
    
    @Operation(summary = "Create an order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Order created", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_CREATE')")
    @PostMapping("")
    public CompletableFuture<ResponseEntity<String>> createOrder() {
    	return createOrder(UUID.randomUUID());
    }

    @Operation(summary = "Create an order with order ID")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Order created", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })

    //TODO: @PreAuthorize("hasRole('PRF_ORDER_CREATE')")
    @PostMapping("/{order-id}")
    public CompletableFuture<ResponseEntity<String>> createOrder(@PathVariable("order-id") UUID orderId) 
    {
    	JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    	UUID userId = UUID.fromString(jwt.getToken().getSubject());
    	CreateOrderCommand command = CreateOrderCommand.builder().orderId(orderId).userId(userId).build();
    	log.info("Executing command: {}", command);
        return commandGateway.send(command).thenApply(it -> ResponseEntity.status(HttpStatus.CREATED).body(it.toString()));
    }

    @Operation(summary = "Add product in order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Product added", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_ADD_PRODUCT')")    
    @PostMapping("/{order-id}/product/{product-id}")
    public CompletableFuture<Void> addProduct(@PathVariable("order-id") UUID orderId,
                                              @PathVariable("product-id") UUID productId) {
    	AddProductCommand command = new AddProductCommand(orderId, productId);
    	log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @Operation(summary = "Increment product quantity by one in order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Product incremented", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })    
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_INCREMENT_PRODUCT')")    
    @PutMapping("/{order-id}/product/{product-id}/increment")
    public CompletableFuture<Void> incrementProduct(@PathVariable("order-id") UUID orderId,
                                                    @PathVariable("product-id") UUID productId) {
    	IncrementProductCountCommand command = new IncrementProductCountCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @Operation(summary = "Decrement product quantity by one in order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Product decremented", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })    
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_DECREMENT_PRODUCT')")    
    @PutMapping("/{order-id}/product/{product-id}/decrement")
    public CompletableFuture<Void> decrementProduct(@PathVariable("order-id") UUID orderId,
                                                    @PathVariable("product-id") UUID productId) {
    	DecrementProductCountCommand command = new DecrementProductCountCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }
    
    @Operation(summary = "Remove product in order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Product removed", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })    
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_REMOVE_PRODUCT')")    
    @DeleteMapping("/{order-id}/product/{product-id}")
    public CompletableFuture<Void> removeProduct(@PathVariable("order-id") UUID orderId,
            									@PathVariable("product-id") UUID productId) {
    	RemoveProductCommand command = new RemoveProductCommand(orderId, productId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @Operation(summary = "Confirm order")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Product removed", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UUID.class)) })
    })    
    //TODO: @PreAuthorize("hasRole('PRF_ORDER_CONFIRM')")    
    @PutMapping("/{order-id}/confirm")
    public CompletableFuture<Object> confirmOrder(@PathVariable("order-id") UUID orderId) {
    	ConfirmOrderCommand command = new ConfirmOrderCommand(orderId);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);//.exceptionally(e -> getErrorResponse(e));
    }
}