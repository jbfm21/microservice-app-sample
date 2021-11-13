package br.com.example.microservice.productreview.infraestructure.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;

import br.com.example.microservice.infraestructure.exceptions.BaseRestExceptionHandler;

@ControllerAdvice
public class BusinessExceptionRestHandler extends BaseRestExceptionHandler {
}
