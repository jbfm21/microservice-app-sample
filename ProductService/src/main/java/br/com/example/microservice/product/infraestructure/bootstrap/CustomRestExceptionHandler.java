package br.com.example.microservice.product.infraestructure.bootstrap;

import org.springframework.web.bind.annotation.ControllerAdvice;

import br.com.example.microservice.infraestructure.exceptions.BaseRestExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends BaseRestExceptionHandler {

}
