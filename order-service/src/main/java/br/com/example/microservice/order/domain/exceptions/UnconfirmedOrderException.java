package br.com.example.microservice.order.domain.exceptions;

public class UnconfirmedOrderException extends IllegalStateException {

	private static final long serialVersionUID = -3979472804142888314L;

	public UnconfirmedOrderException() {
        super("Cannot ship an order which has not been confirmed.");
    }
}
