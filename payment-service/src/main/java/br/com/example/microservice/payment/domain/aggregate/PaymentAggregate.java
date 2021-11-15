package br.com.example.microservice.payment.domain.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import br.com.example.microservice.payment.domain.PaymentStatus;
import br.com.example.microservice.payment.domain.command.CancelPaymentCommand;
import br.com.example.microservice.payment.domain.command.ValidatePaymentCommand;
import br.com.example.microservice.payment.domain.event.PaymentCancelledEvent;
import br.com.example.microservice.payment.domain.event.PaymentCreatedEvent;
import br.com.example.microservice.payment.domain.event.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Aggregate
@Log4j2
@ToString @NoArgsConstructor
public class PaymentAggregate {

	@AggregateIdentifier
    private UUID paymentId;
    private UUID orderId;
    private PaymentStatus paymentStatus;

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand createInvoiceCommand)
    {
        AggregateLifecycle.apply(PaymentProcessedEvent.builder().paymentId(createInvoiceCommand.getPaymentId())
        														.orderId(createInvoiceCommand.getOrderId()).build());
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) 
    {
        AggregateLifecycle.apply(PaymentCancelledEvent.builder().paymentId(cancelPaymentCommand.getPaymentId())
        														.orderId(cancelPaymentCommand.getOrderId())
        														.paymentStatus(cancelPaymentCommand.getPaymentStatus())
        														.build());        
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }    
	
}
