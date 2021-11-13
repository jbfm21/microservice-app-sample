package br.com.example.microservice.product.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.example.microservice.infraestructure.exceptions.ApiException;

public class BusinessExceptions 
{
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductNotFoundException extends ApiException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
}
