package br.com.example.microservice.order.infraestructure.bootstrap;

import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("rabbitMQSpringAMQPMessageSource")
public class RabbitMQMessageSource extends SpringAMQPMessageSource {

	@Autowired
    public RabbitMQMessageSource(final AMQPMessageConverter messageConverter) {
        super(messageConverter);
    }

    @RabbitListener(queues = "${axon.amqp.queue:order.service}")
    @Override
    public void onMessage(final Message message, final Channel channel) {
        log.info("received message: message={}, channel={}", message, channel);
        super.onMessage(message, channel);
    }
}