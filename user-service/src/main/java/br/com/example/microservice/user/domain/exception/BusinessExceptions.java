package br.com.example.microservice.user.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.example.microservice.infraestructure.exceptions.ApiException;

public class BusinessExceptions
{
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class UserNotFoundException extends ApiException {

		private static final long serialVersionUID = 1853987029277230553L;		
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class CardDetailNotFoundException extends ApiException {

		private static final long serialVersionUID = 5355213136778646204L;

	}
	
}

