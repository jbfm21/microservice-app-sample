package br.com.example.microservice.order.domain.saga;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.example.microservice.order.client.user.UserServiceClient;
import br.com.example.microservice.order.domain.event.OrderConfirmedEvent;
import br.com.example.microservice.shopdomain.command.CancelOrderCommand;
import br.com.example.microservice.shopdomain.command.CancelPaymentCommand;
import br.com.example.microservice.shopdomain.command.CompleteOrderCommand;
import br.com.example.microservice.shopdomain.command.ShipOrderCommand;
import br.com.example.microservice.shopdomain.command.ValidatePaymentCommand;
import br.com.example.microservice.shopdomain.event.OrderCancelledEvent;
import br.com.example.microservice.shopdomain.event.OrderCompletedEvent;
import br.com.example.microservice.shopdomain.event.OrderShippedEvent;
import br.com.example.microservice.shopdomain.event.PaymentCancelledEvent;
import br.com.example.microservice.shopdomain.event.PaymentProcessedEvent;
import lombok.extern.log4j.Log4j2;

@Saga
@Log4j2
public class OrderManagementSaga 
{
		@Autowired 
	    private transient CommandGateway commandGateway;
	   	
	   	@Autowired 
	   	UserServiceClient userServiceClient;
	   	
	    @StartSaga
	    @SagaEventHandler(associationProperty = "orderId")
	    private void handle(OrderConfirmedEvent event) 
	    {
	        log.info("OrderConfirmedEvent in Saga for Order Id : {}",event.getOrderId());

	        try {
		        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
						.cardDetails(event.getCardDetails())
						.orderId(event.getOrderId())
						.paymentId(UUID.randomUUID())
						.build();
		        		commandGateway.sendAndWait(validatePaymentCommand);

	        } catch (Exception e) 
	        {
	            //TODO: melhorar log
	        	log.error(e.getMessage());
	            //Start the Compensating transaction
	            cancelOrderCommand(event.getOrderId());
	        }

	    }

	    private void cancelOrderCommand(UUID orderId) 
	    {
	        commandGateway.send(CancelOrderCommand.builder().orderId(orderId).build());
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    private void handle(PaymentProcessedEvent event) 
	    {
	        log.info("PaymentProcessedEvent in Saga for Order Id : {}", event.getOrderId());
	        try {

	        	/*if(true)
	                throw new Exception();*/

	            ShipOrderCommand shipOrderCommand = ShipOrderCommand
	                    .builder()
	                    	.shippingId(UUID.randomUUID())
	                    	.orderId(event.getOrderId())
	                    	.paymentId(event.getPaymentId())
	                    	.build();
	            commandGateway.send(shipOrderCommand);
	        } catch (Exception e) 
	        {
	            //TODO: melhorar log
	        	log.error(e.getMessage());
	            // Start the compensating transaction
	            cancelPaymentCommand(event);
	        }
	    }
	    private void cancelPaymentCommand(PaymentProcessedEvent event) 
	    {
	        commandGateway.send(CancelPaymentCommand.builder()
	        		.paymentId(event.getPaymentId())
	        		.orderId(event.getOrderId()).build());
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    public void handle(OrderShippedEvent event) {

	        log.info("OrderShippedEvent in Saga for Order Id : {}", event.getOrderId());

	        CompleteOrderCommand completeOrderCommand
	                = CompleteOrderCommand.builder()
	                .orderId(event.getOrderId())
	                .build();

	        commandGateway.send(completeOrderCommand);
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    @EndSaga
	    public void handle(OrderCompletedEvent event) 
	    {
	        log.info("OrderCompletedEvent in Saga for Order Id : {}", event.getOrderId());
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    @EndSaga
	    public void handle(OrderCancelledEvent event) 
	    {
	        log.info("OrderCancelledEvent in Saga for Order Id : {}", event.getOrderId());
	    }

	    @SagaEventHandler(associationProperty = "orderId")
	    public void handle(PaymentCancelledEvent event)
	    {
	        log.info("PaymentCancelledEvent in Saga for Order Id : {}", event.getOrderId());
	        cancelOrderCommand(event.getOrderId());
	    }	   	
}
