package br.com.example.microservice.order.infraestructure.interceptor;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

//TODO: review security issues to store jwt in metadata
//This interceptor is used to pass jwt in metadata structure to be captured by the commands that need to call others microservices
public class JwtMessageDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> 
{
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) 
	{
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		
		return (index, message) -> message.andMetaData(Collections.singletonMap("jwt", jwtAuthenticationToken.getToken().getTokenValue()));
	}

}
