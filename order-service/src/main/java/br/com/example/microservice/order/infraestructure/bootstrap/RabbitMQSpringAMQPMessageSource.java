package br.com.example.microservice.order.infraestructure.bootstrap;

import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("rabbitMQSpringAMQPMessageSource")
public class RabbitMQSpringAMQPMessageSource extends SpringAMQPMessageSource {

    @Autowired
    public RabbitMQSpringAMQPMessageSource(final AMQPMessageConverter messageConverter) {
        super(messageConverter);
    }

    @RabbitListener(queues = "${axon.amqp.exchange:order.events}")
    @Override
    public void onMessage(final Message message, final Channel channel) {
        log.debug("received message: message={}, channel={}", message, channel);
        super.onMessage(message, channel);
    }

}