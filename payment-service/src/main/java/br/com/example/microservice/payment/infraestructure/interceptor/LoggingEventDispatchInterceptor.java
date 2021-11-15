package br.com.example.microservice.payment.infraestructure.interceptor;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingEventDispatchInterceptor implements MessageDispatchInterceptor<EventMessage<?>> {

	 @Override
	 public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(List<? extends EventMessage<?>> messages) 
	 {
	        return (index, event) -> 
	        {
	            GenericDomainEventMessage<?>  a = (GenericDomainEventMessage) event;
	        	log.info("Publishing event [{}]: {}.", a.getPayloadType().getClass().getSimpleName(), a);
	            return event;
	        };
	    }
	
}
