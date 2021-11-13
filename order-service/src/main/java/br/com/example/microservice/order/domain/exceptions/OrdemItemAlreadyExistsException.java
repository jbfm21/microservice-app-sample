package br.com.example.microservice.order.domain.exceptions;

public class OrdemItemAlreadyExistsException extends IllegalStateException 
{
	private static final long serialVersionUID = -663760483201317406L;

	public OrdemItemAlreadyExistsException(String productId) {
        super(String.format("Order item for product %s already exists", productId));
    }
}