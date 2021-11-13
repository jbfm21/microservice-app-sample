package br.com.example.microservice.order.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class ProductAddedEvent {
	  private final String orderId;
	  private final String productId;
}