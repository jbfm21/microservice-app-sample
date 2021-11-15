package br.com.example.microservice.order.domain.query;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import br.com.example.microservice.order.client.ProductDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderItemDTO {;
	
	private interface ProductId { @Positive UUID getProductId(); }
	private interface Quantity { @NotBlank Long getQuantity(); }
	private interface ProductInfo {@NotBlank ProductDTO getProductInfo(); }

	public enum Response{;
		@Data @NoArgsConstructor  
		public static class Public implements ProductId, Quantity, ProductInfo  {
	        UUID productId;
	        Long quantity;
	        ProductDTO productInfo;
		}
	}
}
