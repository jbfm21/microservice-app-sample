package br.com.example.microservice.order.infraestructure.bootstrap;

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationConfiguration {

    @Bean
    public SnapshotTriggerDefinition orderAggregateSnapshotTriggerDefinition(Snapshotter snapshotter,
                                                                             @Value("${axon.aggregate.order.snapshot-threshold:10}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }
}