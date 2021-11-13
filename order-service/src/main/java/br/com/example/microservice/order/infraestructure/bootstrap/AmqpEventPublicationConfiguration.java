package br.com.example.microservice.order.infraestructure.bootstrap;



import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Message;
import com.rabbitmq.client.Channel;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;

import lombok.extern.log4j.Log4j2;


//@Configuration
//@Log4j2
public class AmqpEventPublicationConfiguration {

/*    @Value("${axon.amqp.exchange:order.events}")
    String exchangeName;

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.fanoutExchange(exchangeName).build();
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(exchangeName).build();
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("*").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin amqpAdmin, Exchange exchange, Queue queue, Binding binding){
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
    
    @Bean
    public SpringAMQPMessageSource complaintOrderEventsMethod(Serializer serializer) {
        return new SpringAMQPMessageSource(DefaultAMQPMessageConverter.builder() .serializer(serializer).build()) {

            @RabbitListener(queues = "${axon.amqp.exchange:order.events}")
            @Override
            public void onMessage(Message message, Channel channel) {
                log.debug("Event Received: {}", message.getBody().toString());
                super.onMessage(message, channel);
            }
        };
    }*/
}