package br.com.example.microservice.productreview.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.example.microservice.infraestructure.exceptions.ApiException;

public class BusinessExceptions 
{
	private BusinessExceptions()
	{
	}
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductReviewNotFoundException extends ApiException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
	  
   
}
