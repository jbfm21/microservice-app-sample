package br.com.example.microservice.product.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionHandlers 
{
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) 
     {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> 
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
	 
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductNotFoundException extends RuntimeException {
		private static final long serialVersionUID = -1547073032113405697L;
	}
}
