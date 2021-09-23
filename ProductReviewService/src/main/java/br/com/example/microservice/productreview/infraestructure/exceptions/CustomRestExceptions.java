package br.com.example.microservice.productreview.infraestructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.example.microservice.productreview.infraestructure.exceptions.CustomRestExceptions.ApiException;

public class CustomRestExceptions 
{
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductReviewNotFoundException extends ApiException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
	
	public static class ApiException extends RuntimeException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
	
	  
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public static class MicroserviceUnavailableException extends ApiException {

		private static final long serialVersionUID = 7310312040927768939L;
    }
}
