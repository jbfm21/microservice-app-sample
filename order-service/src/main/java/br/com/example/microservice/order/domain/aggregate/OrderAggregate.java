package br.com.example.microservice.order.domain.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.example.microservice.order.client.product.ProductDTO;
import br.com.example.microservice.order.client.product.ProductServiceClient;
import br.com.example.microservice.order.client.user.UserDTO;
import br.com.example.microservice.order.client.user.UserServiceClient;
import br.com.example.microservice.order.domain.OrderStatus;
import br.com.example.microservice.order.domain.command.AddProductCommand;
import br.com.example.microservice.order.domain.command.ConfirmOrderCommand;
import br.com.example.microservice.order.domain.command.CreateOrderCommand;
import br.com.example.microservice.order.domain.command.RemoveProductCommand;
import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.OrderCreatedEvent;
import br.com.example.microservice.order.domain.event.ProductAddedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exception.BusinessException;
import br.com.example.microservice.order.domain.exception.BusinessException.CardDetailNotFoundException;
import br.com.example.microservice.order.domain.exception.BusinessException.OrderAlreadyConfirmedException;
import br.com.example.microservice.shopdomain.command.CancelOrderCommand;
import br.com.example.microservice.shopdomain.command.CompleteOrderCommand;
import br.com.example.microservice.shopdomain.event.OrderCancelledEvent;
import br.com.example.microservice.shopdomain.event.OrderCompletedEvent;
import br.com.example.microservice.shopdomain.model.CardDetailsDTO;
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
	
	private UUID userId;
    
	private OrderStatus orderStatus;
    
    @AggregateMember
    private Map<UUID, OrderItem> orderItems;

    @CommandHandler // Usada para indicar que o método/construto como manipulador de um comando no barramento do Axon.
    public OrderAggregate(CreateOrderCommand command) {
        apply(OrderCreatedEvent.builder().orderId(command.getOrderId()).userId(command.getUserId()).build());
    }
    @EventSourcingHandler //Anotação que marca um método em um agregado como um manipulador para eventos gerados por esse agregado. É usada para ler todos os eventos e montar o estado atual do agregado quando um comando é recebido por exemplo.
    public void on(OrderCreatedEvent event) 
    {
    	this.orderId = event.getOrderId();
        this.orderStatus = OrderStatus.CREATED;
        this.userId = event.getUserId();
        this.orderItems = new HashMap<>();     
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }    
    
    @CommandHandler
    public void handle(ConfirmOrderCommand command, @MetaDataValue(required = true, value = "jwt") String jwt, @Autowired UserServiceClient userServiceClient) 
    {
    	if (this.orderStatus == OrderStatus.CONFIRMED) 
    	{
    		throw new BusinessException.OrderAlreadyConfirmedException(orderId); 
        }
    	UserDTO user = userServiceClient.getUser(String.format("Bearer %s", jwt));
    	
    	if (user == null)
    	{
    		throw new CardDetailNotFoundException();
    	}
	        
        //TODO: Make this better...
        CardDetailsDTO cardDetailDTOShop = CardDetailsDTO.builder()
        												 .cardNumber(user.getCardDetails().getCardNumber())
        												 .cvv(user.getCardDetails().getCvv())
        												 .name(user.getCardDetails().getName())
        												 .validUntilMonth(user.getCardDetails().getValidUntilMonth())
        												 .validUntilYear(user.getCardDetails().getValidUntilYear())
        												 .build();
	        				
    	
    	
        apply(OrderConfirmedEvent.builder().orderId(command.getOrderId()).cardDetails(cardDetailDTOShop).build());
    }
    /*TODO: Conflic with SagaHandler - @EventSourcingHandler
    public void on(OrderConfirmedEvent  event)  
    {
    	this.orderStatus = OrderStatus.CONFIRMED;
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }*/
    
    @CommandHandler
    public void handle(CompleteOrderCommand command) {
        //Validate The Command
        // Publish Order Completed Event
        OrderCompletedEvent event = OrderCompletedEvent.builder()
                .orderId(command.getOrderId())
                .build();
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = OrderStatus.SHIPPED;
    }    
    
    @CommandHandler
    public void handle(CancelOrderCommand command) {
    	OrderCancelledEvent event = OrderCancelledEvent.builder()
        											  .orderId(command.getOrderId())
        											  .build();
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.orderStatus = OrderStatus.CANCELLED;
    }   

    @CommandHandler
    public void handle(AddProductCommand command, @MetaDataValue(required = true, value = "jwt") String jwt, @Autowired ProductServiceClient productServiceClient) 
    {
    	if (this.orderStatus == OrderStatus.CONFIRMED) 
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
    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
    	UUID productId = event.getProduct().getProductId();
    	this.orderItems.put(productId, new OrderItem(productId, event.getProduct().getPrice()));
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
    
    @CommandHandler
    public void handle(RemoveProductCommand command) 
    {
    	if (this.orderStatus == OrderStatus.CONFIRMED) 
    	{
    		throw new BusinessException.OrderAlreadyConfirmedException(orderId);
        }
    	UUID productId = command.getProductId();
        if (!orderItems.containsKey(productId)) {
            throw new BusinessException.OrderItemNotFoundException(command.getOrderId(), productId);
        }
        apply(ProductRemovedEvent.builder().orderId(command.getOrderId()).productId(command.getProductId()).build());
    }
    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
    	this.orderItems.remove(event.getProductId());
    	log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
    
}