package br.com.example.microservice.infraestructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MicroserviceUnavailableException extends ApiException {

	private static final long serialVersionUID = 7310312040927768939L;
}