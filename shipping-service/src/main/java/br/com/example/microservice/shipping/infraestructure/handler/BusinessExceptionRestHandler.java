package br.com.example.microservice.shipping.infraestructure.handler;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.example.microservice.infraestructure.exceptions.ApiError;
import br.com.example.microservice.infraestructure.exceptions.BaseRestExceptionHandler;
import br.com.example.microservice.shipping.domain.exception.BusinessError;
import br.com.example.microservice.shipping.domain.exception.BusinessErrorCode;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class BusinessExceptionRestHandler extends BaseRestExceptionHandler {
	
	
	@ExceptionHandler({ CommandExecutionException.class })
    public ResponseEntity<ApiError> handleCommandExecutionException(final CommandExecutionException ex, final WebRequest request) 
    {
        return ex.getDetails()
           .map((Object it) -> 
           {
        	   	BusinessError businessError = (BusinessError) it;
               	log.error("Unable to process shipping: {}", businessError, ex);
               
               	final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s", businessError.getMessage())).errorCode(businessError.getCode().name()).build();
       		 	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
           })
           .orElseGet(() -> {
               	log.error("Unable to process shipping due to {}", ex.getMessage(), ex);

              	final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s", "Try Again")).errorCode(BusinessErrorCode.UNKNOWN.name()).build();
      		 	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
           });
    }
}