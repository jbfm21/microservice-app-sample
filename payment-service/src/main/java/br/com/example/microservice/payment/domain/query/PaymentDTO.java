package br.com.example.microservice.payment.domain.query;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum PaymentDTO 
{;
	private interface PaymentId { @Positive UUID getPaymentId(); }
	private interface OrderId { @Positive UUID getOrderId(); }
    private interface InvoceStatus { @NotBlank String getInvoiceStatus(); }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements PaymentId, OrderId, InvoceStatus {
    		UUID paymentId;
            UUID orderId;
            String invoiceStatus;
        }
    }
}