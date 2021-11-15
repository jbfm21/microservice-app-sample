package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import br.com.example.microservice.order.client.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Value @Builder @AllArgsConstructor
public class ProductAddedEvent implements Serializable{
	private static final long serialVersionUID = -7198502400861404497L;
	private UUID orderId;
	private ProductDTO product;
}
