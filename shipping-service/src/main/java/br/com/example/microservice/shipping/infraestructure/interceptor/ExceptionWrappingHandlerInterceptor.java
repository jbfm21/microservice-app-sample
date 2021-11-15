package br.com.example.microservice.shipping.infraestructure.interceptor;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.AggregateNotFoundException;

import br.com.example.microservice.shipping.domain.exception.BusinessError;
import br.com.example.microservice.shipping.domain.exception.BusinessErrorCode;
import br.com.example.microservice.shipping.domain.exception.BusinessException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionWrappingHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> 
{

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        try {
            return interceptorChain.proceed();
        } catch (Throwable e) {
            throw new CommandExecutionException("An exception has occurred during command execution", e, exceptionDetails(e));
        }
    }

    private BusinessError exceptionDetails(Throwable throwable) 
    {
        if (throwable instanceof BusinessException) 
        {
        	BusinessException gce = (BusinessException) throwable;
            BusinessError businessError = new BusinessError(gce.getClass().getName(), gce.getErrorCode(),gce.getErrorMessage());
            log.error("Cast BusinessException to {}", businessError, throwable);
            return businessError;
        } if (throwable instanceof AggregateNotFoundException)
        {
        	AggregateNotFoundException aex = (AggregateNotFoundException) throwable;   
        	BusinessError businessError = new BusinessError(throwable.getClass().getName(), BusinessErrorCode.SHIPPING_NOT_FOUND, String.format("Shipping not found"));
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