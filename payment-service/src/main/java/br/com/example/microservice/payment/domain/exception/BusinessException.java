package br.com.example.microservice.payment.domain.exception;

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
	public static class PaymentNotFoundException extends BusinessException 
	{
		private static final long serialVersionUID = -6097466475666041785L;

		public PaymentNotFoundException(UUID paymentId) {super(String.format("Payment [%s] not found", paymentId), BusinessErrorCode.PAYMENT_NOT_FOUND); }
	}	 

}
