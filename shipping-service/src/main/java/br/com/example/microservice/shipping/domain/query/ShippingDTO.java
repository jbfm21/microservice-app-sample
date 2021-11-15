package br.com.example.microservice.shipping.domain.query;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum ShippingDTO 
{;
    private interface ShippingId { @NotBlank UUID getShippingId(); }
	private interface PaymentId { @NotBlank UUID getPaymentId(); }
	private interface OrderId { @NotBlank  UUID getOrderId(); }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements ShippingId, OrderId, PaymentId {
    		UUID shippingId;
            UUID orderId;
    		UUID paymentId;
        }
    }
}