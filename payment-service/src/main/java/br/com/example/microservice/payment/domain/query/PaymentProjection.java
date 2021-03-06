package br.com.example.microservice.payment.domain.query;

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

import br.com.example.microservice.payment.domain.PaymentStatus;
import br.com.example.microservice.payment.domain.exception.BusinessException;
import br.com.example.microservice.payment.infraestructure.entity.PaymentEntity;
import br.com.example.microservice.payment.infraestructure.repository.PaymentRepository;
import br.com.example.microservice.shopdomain.event.PaymentCancelledEvent;
import br.com.example.microservice.shopdomain.event.PaymentProcessedEvent;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
//@ProcessingGroup("amqpEvents") //Usamos essa anotação para especificar que os eventos serão consumidos do RabbitMQ.
@Transactional
public class PaymentProjection {

	private final PaymentRepository paymentRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public PaymentProjection(PaymentRepository paymentRepository, ModelMapper modelMapper)
	{
		this.paymentRepository = paymentRepository;
		this.modelMapper = modelMapper;
	}
	
	@EventHandler //Anotação usada para especificar um método manipulador de evento. O método deve receber como parâmetro o evento que deseja escutar.
	public void on(PaymentProcessedEvent event) 
	{
		PaymentEntity payment = PaymentEntity.builder().paymentId(event.getPaymentId())
				
													   .orderId(event.getOrderId())
														//TODO: make this better
													   .paymentStatus(PaymentStatus.PAID).build();
        paymentRepository.save(payment);
        log.info("A payment was added! {}", payment);
	}

	@EventHandler
    public void on(PaymentCancelledEvent event) {
        PaymentEntity payment = paymentRepository.findByIdOrNotFoundException(event.getPaymentId());
		//TODO: make this better
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
    }
 
    @QueryHandler
    public List<PaymentDTO.Response.Public> handle(Queries.FindAllPaymentQuery query) 
    {
    	List<PaymentEntity> payments =  paymentRepository.findAll();
    	return payments.stream().map(p -> modelMapper.map(p, PaymentDTO.Response.Public.class)).toList();
    }
    

	@ExceptionHandler
    public void handle(Exception exception) 
    {
    	log.error("Error handling event. Generic exception: {}", exception.getMessage(), exception);
    }
    
    @ExceptionHandler(resultType = BusinessException.PaymentNotFoundException.class)
    public void handle(BusinessException.PaymentNotFoundException exception) 
    {
    	log.error("Error handling event. Payment not found exception: {}", exception.getMessage(), exception);
    }
    
    @MessageHandlerInterceptor
    public void intercept(Message<?> message) {
    	
    	log.info("Handling query {}: {}", message.getPayloadType().getSimpleName(), message);
    }
    
    
}
