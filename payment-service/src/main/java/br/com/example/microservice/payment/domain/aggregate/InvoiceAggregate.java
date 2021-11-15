package br.com.example.microservice.payment.domain.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.example.microservice.payment.domain.InvoiceStatus;
import br.com.example.microservice.payment.domain.command.CreateInvoiceCommand;
import br.com.example.microservice.payment.domain.event.InvoiceCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Aggregate
@Log4j2
@ToString @NoArgsConstructor
public class InvoiceAggregate {

	@AggregateIdentifier
    private UUID paymentId;
    private UUID orderId;
    private InvoiceStatus invoiceStatus;

    @CommandHandler
    public InvoiceAggregate(CreateInvoiceCommand createInvoiceCommand){
        AggregateLifecycle.apply(new InvoiceCreatedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId));
    }

    @EventSourcingHandler
    protected void on(InvoiceCreatedEvent event){
        this.paymentId = event.paymentId;
        this.orderId = event.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        
        log.info("Event {} handled with {} in {}", event.getClass().getSimpleName(), event, this);
    }
	
}
