package br.com.example.microservice.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomRestExceptions 
{
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductNotFoundException extends RuntimeException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
}
