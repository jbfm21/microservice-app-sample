package br.com.example.microservice.shipping.infraestructure.interceptor;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingCommandMessageDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> 
{

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            log.info("Dispatching a command {}:  {}", command.getClass().getSimpleName(), command);
            return command;
        };
    }
}