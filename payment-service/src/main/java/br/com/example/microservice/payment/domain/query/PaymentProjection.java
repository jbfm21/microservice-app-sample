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

import br.com.example.microservice.payment.domain.InvoiceStatus;
import br.com.example.microservice.payment.domain.event.InvoiceCreatedEvent;
import br.com.example.microservice.payment.domain.exception.BusinessException;
import br.com.example.microservice.payment.infraestructure.entity.PaymentEntity;
import br.com.example.microservice.payment.infraestructure.repository.PaymentRepository;
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
	public void on(InvoiceCreatedEvent event) 
	{
		PaymentEntity payment = PaymentEntity.builder().paymentId(event.getPaymentId()).orderId(event.getOrderId()).status(InvoiceStatus.PAID).build();
        paymentRepository.save(payment);
        log.info("A payment was added! {}", payment);
	}
 
    @QueryHandler
    public List<PaymentDTO.Response.Public> handle(Queries.FindAllPaymentQuery query) 
    {
    	List<PaymentEntity> payments =  paymentRepository.findAll();
    	
    	List<PaymentDTO.Response.Public> resultDTO = payments.stream().map(p -> modelMapper.map(p, PaymentDTO.Response.Public.class)).toList();

    	return resultDTO;
    }
    

	@ExceptionHandler
    public void handle(Exception exception) 
    {
    	log.error("Error handling event. Generic exception: {}", exception.getMessage(), exception);
    }
    
    @ExceptionHandler(resultType = BusinessException.PaymentNotFoundException.class)
    public void handle(BusinessException.PaymentNotFoundException exception) 
    {
    	log.error("Error handling event. Payment not found exception: {}", exception.getMessage(), exception);;
    }
    
    @MessageHandlerInterceptor
    public void intercept(Message<?> message) {
    	
    	log.info("Handling query {}: {}", message.getPayloadType().getSimpleName(), message);
    }
    
    
}
