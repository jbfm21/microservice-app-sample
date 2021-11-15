package br.com.example.microservice.order.infraestructure.interceptor;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;


public class ExampleMessageHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {
	
    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception 
    {
        //Make something before command execution
    	/*CommandMessage<?> command = unitOfWork.getMessage();
        String someData = Optional.ofNullable(command.getMetaData().get("someData"))
                                .map(s -> (String) s)
                                .orElseThrow(null);*/
        return interceptorChain.proceed();
    }
}

