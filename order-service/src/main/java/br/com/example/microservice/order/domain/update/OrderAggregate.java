package br.com.example.microservice.order.domain.update;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.HashMap;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.OrderCreatedEvent;
import br.com.example.microservice.order.domain.event.OrderShippedEvent;
import br.com.example.microservice.order.domain.event.ProductAddedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exceptions.OrdemItemAlreadyExistsException;
import br.com.example.microservice.order.domain.exceptions.OrderAlreadyConfirmedException;
import br.com.example.microservice.order.domain.exceptions.OrderItemNotFoundException;
import br.com.example.microservice.order.domain.exceptions.UnconfirmedOrderException;
import br.com.example.microservice.order.domain.update.command.AddProductCommand;
import br.com.example.microservice.order.domain.update.command.ConfirmOrderCommand;
import br.com.example.microservice.order.domain.update.command.CreateOrderCommand;
import br.com.example.microservice.order.domain.update.command.RemoveProductCommand;
import br.com.example.microservice.order.domain.update.command.ShipOrderCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Aggregate //Usada para representar um agregado, árvore isolada de entidades capaz de manipular comandos. Quando um comando é despachado o Axon carrega a instância e invoca o método manipulador do comando associado.
@Log4j2
public class OrderAggregate {

	@AggregateIdentifier //Campo usado como identificador do agregado. O Axon utilizará esse campo para buscar os eventos relacionados ao agregado e carregar seu estado atual.
	private String orderId;
    
	private boolean orderConfirmed;
    
    @AggregateMember
    private Map<String, OrderItem> orderItems;

    //private BigDecimal totalPrice;
    
    public OrderAggregate() {
    }

    @CommandHandler // Usada para indicar que o método/construto como manipulador de um comando no barramento do Axon.
    public OrderAggregate(CreateOrderCommand command) {
    	
    	log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
    	
        apply(OrderCreatedEvent.builder().orderId(command.getOrderId()).build());
    }
    
    @CommandHandler
    public void handle(ConfirmOrderCommand command) 
    {
    	log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
    	if (orderConfirmed) {
             return;
        }
        apply(ConfirmOrderCommand.builder().orderId(command.getOrderId()).build());
    }
   

    @CommandHandler
    public void handle(ShipOrderCommand  command) 
    {
    	log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
    	if (!orderConfirmed) 
    	{
    		throw new UnconfirmedOrderException();
        }
    	apply(OrderShippedEvent.builder().orderId(command.getOrderId()).build());
    }


    @CommandHandler
    public void handle(AddProductCommand command) 
    {
    	log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
    	if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(orderId);
        }
    	String productId = command.getProductId();
        if (orderItems.containsKey(productId)) {
            throw new OrdemItemAlreadyExistsException(productId);
        }
        apply(ProductAddedEvent.builder().orderId(command.getOrderId()).productId(command.getProductId()).build());
    }
    
    @CommandHandler
    public void handle(RemoveProductCommand command) 
    {
    	log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
    	if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(orderId);
        }
    	String productId = command.getProductId();
        if (!orderItems.containsKey(productId)) {
            throw new OrderItemNotFoundException(command.getOrderId(), productId);
        }
        apply(ProductRemovedEvent.builder().orderId(command.getOrderId()).productId(command.getProductId()).build());
    }

    @EventSourcingHandler //Anotação que marca um método em um agregado como um manipulador para eventos gerados por esse agregado. É usada para ler todos os eventos e montar o estado atual do agregado quando um comando é recebido por exemplo.
    public void on(OrderCreatedEvent event) 
    {
    	//Attention: this event need to set all aggregate properties, because the framework use this event to create a new aggregate and to load an existing aggregate
    	//Attention 2: nned to be the first eventsourcing handler
    	log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
    	this.orderId = event.getOrderId();
        this.orderConfirmed = event.isOrderConfirmed();
        this.orderItems = (event.getOrderItems() == null) ? new HashMap<>() : event.getOrderItems();
    }
    
    
    @EventSourcingHandler
    public void on(OrderConfirmedEvent  event)  
    {
    	log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
    	this.orderConfirmed = true;
    }
    
    @EventSourcingHandler
    public void on(OrderShippedEvent event)  
    {
    	log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
    }
    
    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
    	log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
    	String productId = event.getProductId();
    	this.orderItems.put(productId, new OrderItem(productId));
    }
    
    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
    	log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
    	this.orderItems.remove(event.getProductId());
    }
    
}