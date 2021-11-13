package br.com.example.microservice.order.domain.exceptions;

public class OrderAlreadyConfirmedException extends IllegalStateException {

	private static final long serialVersionUID = 957545789606874882L;

	public OrderAlreadyConfirmedException(String orderId) 
    {
        super(String.format("Order %s is already confirmed.", orderId));
    }
}