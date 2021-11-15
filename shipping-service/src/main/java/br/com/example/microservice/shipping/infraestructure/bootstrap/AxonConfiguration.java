package br.com.example.microservice.shipping.infraestructure.bootstrap;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.example.microservice.shipping.infraestructure.interceptor.ExceptionWrappingHandlerInterceptor;
import br.com.example.microservice.shipping.infraestructure.interceptor.LoggingCommandMessageDispatchInterceptor;
import br.com.example.microservice.shipping.infraestructure.interceptor.LoggingEventDispatchInterceptor;

@Configuration
public class AxonConfiguration {

    @Bean
    public SnapshotTriggerDefinition orderAggregateSnapshotTriggerDefinition(Snapshotter snapshotter,
                                                                             @Value("${axon.aggregate.order.snapshot-threshold:10}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }
    
    @Autowired
    void commandBus(EventBus eventBus, CommandBus commandBus, ExceptionWrappingHandlerInterceptor exceptionWrappingHandlerInterceptor, LoggingCommandMessageDispatchInterceptor loggingMessageDispatchInterceptor) {
        commandBus.registerHandlerInterceptor(exceptionWrappingHandlerInterceptor);
        commandBus.registerDispatchInterceptor(loggingMessageDispatchInterceptor);
        
        eventBus.registerDispatchInterceptor(new LoggingEventDispatchInterceptor());
        
    }
    
}