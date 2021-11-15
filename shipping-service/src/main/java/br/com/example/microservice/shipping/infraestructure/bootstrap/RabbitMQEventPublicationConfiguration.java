package br.com.example.microservice.shipping.infraestructure.bootstrap;

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

//Create the queue if not exists, ....
@Configuration
public class RabbitMQEventPublicationConfiguration {

    @Value("${axon.amqp.exchange:order.events}")
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
    
}