package br.com.example.microservice.order.domain.exceptions;

public class OrderNotFoundException extends IllegalStateException {

	
	private static final long serialVersionUID = 17326266742042254L;

	public OrderNotFoundException() 
    {
    }

	
	public OrderNotFoundException(String orderId) 
    {
        super(String.format("Order %s not found.", orderId));
    }
}