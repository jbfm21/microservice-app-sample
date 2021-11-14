package br.com.example.microservice.order.domain.queries;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderItemDTO {;
	
	private interface ProductId { @Positive UUID getProductId(); }
	private interface Quantity { @NotBlank Long getQuantity(); }

	public enum Response{;
		@Data @NoArgsConstructor  
		public static class Public implements ProductId, Quantity  {
	        UUID productId;
	        Long quantity;
			
    }
	}
}
