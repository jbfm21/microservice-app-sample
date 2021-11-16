package br.com.example.microservice.order.domain.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.example.microservice.infraestructure.exceptions.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class BusinessException extends ApiException
{
	private static final long serialVersionUID = -7534083246264889480L;
	private String errorMessage;
	private BusinessErrorCode errorCode;

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class OrderNotFoundException extends BusinessException 
	{
		private static final long serialVersionUID = 7634074969242978202L;
		public OrderNotFoundException(UUID orderId) {super(String.format("Order [%s] not found", orderId), BusinessErrorCode.ORDER_NOT_FOUND); }
	}	 
	 
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class OrderItemNotFoundException extends BusinessException 
	{
		private static final long serialVersionUID = 373570185895589988L;
		public OrderItemNotFoundException(UUID orderId, UUID productId) {super(String.format("Could find product [%s] in order [%s]", orderId, productId), BusinessErrorCode.ORDER_ITEM_NOT_FOUND); }
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ProductNotFoundException extends BusinessException 
	{
		private static final long serialVersionUID = 373570185895589988L;
		public ProductNotFoundException(UUID productId) {super(String.format("Could find product [%s] in catalog", productId), BusinessErrorCode.PRODUCT_NOT_FOUND); }
	}

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class OrdemItemAlreadyExistsException extends BusinessException 
	{
		private static final long serialVersionUID = 8393734100026532669L;
		public OrdemItemAlreadyExistsException(UUID orderId, UUID productId) {super(String.format("Item [%s] already added in order [%s]", productId, orderId), BusinessErrorCode.ORDER_ITEM_ALREADY_EXISTS); }
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class CardDetailNotFoundException extends BusinessException 
	{
		private static final long serialVersionUID = 8393734100026532669L;
		public CardDetailNotFoundException() {super("Could not get user card details", BusinessErrorCode.CARD_DETAIL_NOT_FOUND); }
	}
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class OrderAlreadyConfirmedException extends BusinessException 
	{
		private static final long serialVersionUID = 7815629182950125609L;
		public OrderAlreadyConfirmedException(UUID orderId) {super(String.format("Order [%s] already confirmed", orderId), BusinessErrorCode.ORDER_ALREADY_CONFIRMED); }
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class UnconfirmedOrderException extends BusinessException 
	{
		private static final long serialVersionUID = -5355814632762602555L;
		public UnconfirmedOrderException(UUID orderId) {super(String.format("Order [%s] is not confirmed yet", orderId), BusinessErrorCode.ORDER_UNCONFIRMED); }
	}
	

}
