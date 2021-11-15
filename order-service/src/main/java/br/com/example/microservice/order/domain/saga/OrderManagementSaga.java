package br.com.example.microservice.order.domain.saga;

import java.util.UUID;

import javax.inject.Inject;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import br.com.example.microservice.order.domain.command.CreateInvoiceCommand;
import br.com.example.microservice.order.domain.command.CreateShippingCommand;
import br.com.example.microservice.order.domain.command.ShipOrderCommand;
import br.com.example.microservice.order.domain.event.InvoiceCreatedEvent;
import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.order.domain.event.OrderFinishedEvent;
import br.com.example.microservice.order.domain.event.OrderShippedEvent;
import lombok.extern.log4j.Log4j2;

@Saga
@Log4j2
public class OrderManagementSaga 
{
	   	@Inject
	    private transient CommandGateway commandGateway;

	    @StartSaga
	    @SagaEventHandler(associationProperty = "orderId")
	    public void handle(OrderConfirmedEvent orderConfirmedEvent)
	    {
	    	log.info("Starting saga to create invoce command. Event {}: {}", orderConfirmedEvent.getClass().getSimpleName(), orderConfirmedEvent);	    	
	    	
	    	UUID paymentId = UUID.randomUUID();
	        SagaLifecycle.associateWith("paymentId", paymentId.toString());
	        //send the commands
	        commandGateway.send(new CreateInvoiceCommand(paymentId, orderConfirmedEvent.getOrderId()));
	    }

	    @SagaEventHandler(associationProperty = "paymentId")
	    public void handle(InvoiceCreatedEvent invoiceCreatedEvent)
	    {
	        log.info("Continue saga to create shipping command. Event {}: {}", invoiceCreatedEvent.getClass().getSimpleName(), invoiceCreatedEvent);

	        //associate Saga with shipping
	        UUID shippingId = UUID.randomUUID();
	        SagaLifecycle.associateWith("shipping", shippingId.toString());

	        //send the create shipping command
	        commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.getOrderId(), invoiceCreatedEvent.getPaymentId()));
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    public void handle(OrderShippedEvent orderShippedEvent)
	    {
	        commandGateway.send(new ShipOrderCommand(orderShippedEvent.getOrderId()));
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    public void handle(OrderFinishedEvent orderFinishedEvent){
	        SagaLifecycle.end();
	    }
}
