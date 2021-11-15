package br.com.example.microservice.order.domain.update;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.example.microservice.order.client.ProductDTO;
import br.com.example.microservice.order.client.ProductServiceClient;
import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.OrderCreatedEvent;
import br.com.example.microservice.order.domain.event.OrderShippedEvent;
import br.com.example.microservice.order.domain.event.ProductAddedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exceptions.BusinessException;
import br.com.example.microservice.order.domain.exceptions.BusinessException.OrderAlreadyConfirmedException;
import br.com.example.microservice.order.domain.update.command.AddProductCommand;
import br.com.example.microservice.order.domain.update.command.ConfirmOrderCommand;
import br.com.example.microservice.order.domain.update.command.CreateOrderCommand;
import br.com.example.microservice.order.domain.update.command.RemoveProductCommand;
import br.com.example.microservice.order.domain.update.command.ShipOrderCommand;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Aggregate //Usada para representar um agregado, árvore isolada de entidades capaz de manipular comandos. Quando um comando é despachado o Axon carrega a instância e invoca o método manipulador do comando associado.
@Log4j2
@ToString @NoArgsConstructor
public class OrderAggregate {

	//Campo usado como identificador do agregado. O Axon utilizará esse campo para buscar os eventos relacionados ao agregado e carregar seu estado atual.
	@AggregateIdentifier 
	private UUID orderId;
    
	private boolean orderConfirmed;
    
    @AggregateMember
    private Map<UUID, OrderItem> orderItems;

    @CommandHandler // Usada para indicar que o método/construto como manipulador de um comando no barramento do Axon.
    public OrderAggregate(CreateOrderCommand command) {
        apply(OrderCreatedEvent.builder().orderId(command.getOrderId()).build());
    }
    
    @CommandHandler
    public void handle(ConfirmOrderCommand command) 
    {
    	if (this.orderConfirmed) 
    	{
    		throw new BusinessException.OrderAlreadyConfirmedException(orderId); 
        }
    	
        apply(OrderConfirmedEvent.builder().orderId(command.getOrderId()).build());
    }
   

    @CommandHandler
    public void handle(ShipOrderCommand  command) 
    {
    	if (!orderConfirmed) 
    	{
    		throw new BusinessException.UnconfirmedOrderException(command.getOrderId());
        }
    	apply(OrderShippedEvent.builder().orderId(orderId).build());
    }


    @CommandHandler
    public void handle(AddProductCommand command, @MetaDataValue(required = true, value = "jwt") String jwt, ProductServiceClient productServiceClient) 
    {
    	if (orderConfirmed) 
    	{
            throw new OrderAlreadyConfirmedException(orderId);
        }
    	UUID productId = command.getProductId();
        if (orderItems.containsKey(productId)) {
            throw new BusinessException.OrdemItemAlreadyExistsException(orderId, productId);
        }
        
        //Example of calling another microservice to get some information
        //TODO: Use REDIS CACHE ?
        ProductDTO product = productServiceClient.getProduct(String.format("Bearer %s", jwt), command.getProductId());
        
        if (product == null)
        {
        	throw new BusinessException.ProductNotFoundException(productId);	
        }
        
        apply(ProductAddedEvent.builder().orderId(command.getOrderId()).product(product).build());
    }
    
    @CommandHandler
    public void handle(RemoveProductCommand command) 
    {
    	if (orderConfirmed) 
    	{
    		throw new BusinessException.OrderAlreadyConfirmedException(orderId);
        }
    	UUID productId = command.getProductId();
        if (!orderItems.containsKey(productId)) {
            throw new BusinessException.OrderItemNotFoundException(command.getOrderId(), productId);
        }
        apply(ProductRemovedEvent.builder().orderId(command.getOrderId()).productId(command.getProductId()).build());
    }

    @EventSourcingHandler //Anotação que marca um método em um agregado como um manipulador para eventos gerados por esse agregado. É usada para ler todos os eventos e montar o estado atual do agregado quando um comando é recebido por exemplo.
    public void on(OrderCreatedEvent event) 
    {
    	this.orderId = event.getOrderId();
        this.orderConfirmed = false;
        this.orderItems = new HashMap<>();     
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
    
    @EventSourcingHandler
    public void on(OrderConfirmedEvent  event)  
    {
    	this.orderConfirmed = true;
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
    @EventSourcingHandler
    public void on(OrderShippedEvent event)  
    {
    	this.orderConfirmed = true;
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
    	UUID productId = event.getProduct().getProductId();
    	this.orderItems.put(productId, new OrderItem(productId, event.getProduct().getPrice()));
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
    	this.orderItems.remove(event.getProductId());
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
}