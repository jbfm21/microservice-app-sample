package br.com.example.microservice.order.domain.exceptions;

public class OrderItemNotFoundException extends IllegalStateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5935103220452445318L;

	public OrderItemNotFoundException(String orderId, String productId) 
    {
        super(String.format("Order item %s / %s not found.", orderId, productId));
    }
}