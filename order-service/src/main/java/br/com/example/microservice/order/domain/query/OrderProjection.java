package br.com.example.microservice.order.domain.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.messaging.interceptors.MessageHandlerInterceptor;
import org.axonframework.queryhandling.QueryHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.example.microservice.order.client.product.ProductDTO;
import br.com.example.microservice.order.domain.OrderStatus;
import br.com.example.microservice.order.domain.event.OrderCancelledEvent;
import br.com.example.microservice.order.domain.event.OrderCompletedEvent;
import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.OrderCreatedEvent;
import br.com.example.microservice.order.domain.event.OrderShippedEvent;
import br.com.example.microservice.order.domain.event.ProductAddedEvent;
import br.com.example.microservice.order.domain.event.ProductCountDecrementedEvent;
import br.com.example.microservice.order.domain.event.ProductCountIncrementedEvent;
import br.com.example.microservice.order.domain.event.ProductRemovedEvent;
import br.com.example.microservice.order.domain.exception.BusinessException;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;
import br.com.example.microservice.order.infraestructure.entity.OrderItemEntity;
import br.com.example.microservice.order.infraestructure.repository.OrderItemRepository;
import br.com.example.microservice.order.infraestructure.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
//@ProcessingGroup("amqpEvents") //Usamos essa anotação para especificar que os eventos serão consumidos do RabbitMQ.
@Transactional
public class OrderProjection {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public OrderProjection(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ModelMapper modelMapper)
	{
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.modelMapper = modelMapper;
	}
	 
	@EventHandler //Anotação usada para especificar um método manipulador de evento. O método deve receber como parâmetro o evento que deseja escutar.
	public void on(OrderCreatedEvent event) 
	{
		OrderEntity order = OrderEntity.builder().orderId(event.getOrderId())
												 .userId(event.getUserId())
												 .status(OrderStatus.CREATED).build();
		order.setOrderItems(new ArrayList<>());
        this.orderRepository.save(order);
        log.info("A order was added! {}", order );
	}
	 
	@EventHandler
	public void on(OrderConfirmedEvent event) 
	{
		OrderEntity order = orderRepository.findByIdOrNotFoundException(event.getOrderId());
		order.setStatus(OrderStatus.CONFIRMED);
		orderRepository.save(order);
		log.info("A order was confirmed! {}", order );
	}
	
	
	@EventHandler
    public void on(OrderCompletedEvent event) {
		OrderEntity order = orderRepository.findByIdOrNotFoundException(event.getOrderId());
        order.setStatus(event.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event) 
    {
    	OrderEntity order = orderRepository.findByIdOrNotFoundException(event.getOrderId());
        order.setStatus(event.getOrderStatus());
        orderRepository.save(order);
    }	
	

    //TODO: CONFIRMAR
	@EventHandler
	public void on(OrderShippedEvent event) 
	{
		OrderEntity order = orderRepository.findByIdOrNotFoundException(event.getOrderId());
		order.setStatus(OrderStatus.SHIPPED);
		orderRepository.save(order);
		log.info("A order was shipped! {}", order );
        
	}

	@EventHandler
	public void on(ProductAddedEvent event) 
	{
		OrderEntity order = orderRepository.findByIdOrNotFoundException(event.getOrderId());
		ProductDTO product = event.getProduct();
		boolean containsProduct = order.getOrderItems().stream().anyMatch(p-> p.getProductId().compareTo(product.getProductId()) == 0);
		if (containsProduct)
		{
			  throw new BusinessException.OrdemItemAlreadyExistsException(event.getOrderId(), product.getProductId());
		}
		OrderItemEntity orderItem = OrderItemEntity.builder().order(order).productId(product.getProductId()).price(product.getPrice()).quantity(1L).build();
		orderItemRepository.save(orderItem);
		log.info("A order item was added! {} - {}", order, orderItem );
		
	}

	@EventHandler
	public void on(ProductCountDecrementedEvent event) 
	{
        OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductIdOrNotFoundException(event.getOrderId(), event.getProductId());
        orderItemEntity.decrement();
        
        if (orderItemEntity.getQuantity() <= 0)
        {
        	orderItemRepository.delete(orderItemEntity);
        	log.info("A order item was removed! {}", orderItemEntity );
        	return;
        }
        
        orderItemRepository.save(orderItemEntity);
        log.info("A order item was decremented! {}",orderItemEntity );
	}
	
	@EventHandler
	public void on(ProductCountIncrementedEvent event) 
	{
		OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductIdOrNotFoundException(event.getOrderId(), event.getProductId());
		orderItemEntity.increment();
       	orderItemRepository.save(orderItemEntity);
       	log.info("A order item was incremented! {}",orderItemEntity );
	}
	
	@EventHandler
	public void on(ProductRemovedEvent event) 
	{
        OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductIdOrNotFoundException(event.getOrderId(), event.getProductId());
        orderItemRepository.delete(orderItemEntity);
        log.info("A order item was removed! {}", orderItemEntity );
	}
 
    @QueryHandler
    public List<OrderDTO.Response.Public> handle(Queries.FindAllOrderQuery query) 
    {
    	List<OrderEntity> orders =  this.orderRepository.findAll();
    	
    	List<OrderDTO.Response.Public> resultDTO = orders.stream().map(order -> modelMapper.map(order, OrderDTO.Response.Public.class)).toList();

    	return resultDTO;
    }
    

	@ExceptionHandler
    public void handle(Exception exception) 
    {
    	log.error("Error handling event. Generic exception: {}", exception.getMessage(), exception);
    }
    
    @ExceptionHandler(resultType = BusinessException.OrderNotFoundException.class)
    public void handle(BusinessException.OrderNotFoundException exception) 
    {
    	log.error("Error handling event. Order not found exception: {}", exception.getMessage(), exception);;
    }
    
    @MessageHandlerInterceptor
    public void intercept(Message<?> message) {
    	
    	log.info("Handling query {}: {}", message.getPayloadType().getSimpleName(), message);
    }
    
    
}
