package br.com.example.microservice.order.infraestructure.bootstrap;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.example.microservice.order.infraestructure.interceptor.LoggingEventDispatchInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.ExampleMessageHandlerInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.ExceptionWrappingHandlerInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.JwtMessageDispatchInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.LoggingCommandMessageDispatchInterceptor;

@Configuration
public class AxonConfiguration {

    @Bean
    public SnapshotTriggerDefinition orderAggregateSnapshotTriggerDefinition(Snapshotter snapshotter,
                                                                             @Value("${axon.aggregate.order.snapshot-threshold:10}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }
    
    @Autowired
    void commandBus(EventBus eventBus, CommandBus commandBus, ExceptionWrappingHandlerInterceptor exceptionWrappingHandlerInterceptor, JwtMessageDispatchInterceptor jwtMessageDispatchInterceptor, ExampleMessageHandlerInterceptor exampleMessageHandlerInterceptor, LoggingCommandMessageDispatchInterceptor loggingMessageDispatchInterceptor) {
        commandBus.registerHandlerInterceptor(exceptionWrappingHandlerInterceptor);
        commandBus.registerDispatchInterceptor(loggingMessageDispatchInterceptor);
        commandBus.registerDispatchInterceptor(jwtMessageDispatchInterceptor);
        commandBus.registerHandlerInterceptor(exampleMessageHandlerInterceptor);
        
        eventBus.registerDispatchInterceptor(new LoggingEventDispatchInterceptor());
        
    }
    
}