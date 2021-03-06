package br.com.example.microservice.order.infraestructure.interceptor;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.AggregateNotFoundException;

import br.com.example.microservice.order.domain.exception.BusinessError;
import br.com.example.microservice.order.domain.exception.BusinessErrorCode;
import br.com.example.microservice.order.domain.exception.BusinessException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionWrappingHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> 
{

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        try {
            return interceptorChain.proceed();
        } catch (Exception e) {
            throw new CommandExecutionException("An exception has occurred during command execution", e, exceptionDetails(e));
        }
    }

    private BusinessError exceptionDetails(Throwable throwable) 
    {
        if (throwable instanceof BusinessException gce) 
        {        	
            BusinessError businessError = new BusinessError(gce.getClass().getName(), gce.getErrorCode(),gce.getErrorMessage());
            log.error("Cast BusinessException to {}", businessError, throwable);
            return businessError;
        } 
        if (throwable instanceof AggregateNotFoundException aex)
        {
        	BusinessError businessError = new BusinessError(throwable.getClass().getName(), BusinessErrorCode.ORDER_NOT_FOUND, "Order not found");
            log.error("Cast BusinessException to {}", businessError, aex);
            return businessError;
        }
        else 
        {
            BusinessError businessError = new BusinessError(throwable.getClass().getName(),BusinessErrorCode.UNKNOWN,throwable.getMessage());
            log.error("Cast CommandExecutionException to {}", businessError, throwable);
            return businessError;
        }
    }
}