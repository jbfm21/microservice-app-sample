package br.com.example.microservice.shipping.domain.query;

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

import br.com.example.microservice.shipping.domain.ShippingStatus;
import br.com.example.microservice.shipping.domain.exception.BusinessException;
import br.com.example.microservice.shipping.infraestructure.entity.ShippingEntity;
import br.com.example.microservice.shipping.infraestructure.repository.ShippingRepository;
import br.com.example.microservice.shopdomain.event.OrderShippedEvent;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
//@ProcessingGroup("amqpEvents") //Usamos essa anotação para especificar que os eventos serão consumidos do RabbitMQ.
@Transactional
public class ShippingProjection {

	private final ShippingRepository shippingRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public ShippingProjection(ShippingRepository shippingRepository, ModelMapper modelMapper)
	{
		this.shippingRepository = shippingRepository;
		this.modelMapper = modelMapper;
	}

	
	@EventHandler //Anotação usada para especificar um método manipulador de evento. O método deve receber como parâmetro o evento que deseja escutar.
	public void on(OrderShippedEvent event) 
	{
		ShippingEntity shipping = ShippingEntity.builder()
				.shippingId(event.getShippingId())
				.orderId(event.getOrderId())
				.paymentId(event.getPaymentId())
				//TODO: make this better
				.shippingStatus(ShippingStatus.COMPLETED)
				.build();
		shippingRepository.save(shipping);
        log.info("A shipping was added! {}", shipping);
	}
 
    @QueryHandler
    public List<ShippingDTO.Response.Public> handle(Queries.FindAllShippingQuery query) 
    {
    	List<ShippingEntity> shippings =  shippingRepository.findAll();
    	return shippings.stream().map(s -> modelMapper.map(s, ShippingDTO.Response.Public.class)).toList();
    }
    

	@ExceptionHandler
    public void handle(Exception exception) 
    {
    	log.error("Error handling event. Generic exception: {}", exception.getMessage(), exception);
    }
    
    @ExceptionHandler(resultType = BusinessException.ShippingNotFoundException.class)
    public void handle(BusinessException.ShippingNotFoundException exception) 
    {
    	log.error("Error handling event. Shipping not found exception: {}", exception.getMessage(), exception);
    }
    
    @MessageHandlerInterceptor
    public void intercept(Message<?> message) {
    	
    	log.info("Handling query {}: {}", message.getPayloadType().getSimpleName(), message);
    }
    
    
}
