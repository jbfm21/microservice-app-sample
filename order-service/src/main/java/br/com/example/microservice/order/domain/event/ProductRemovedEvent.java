package br.com.example.microservice.order.domain.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class ProductRemovedEvent implements Serializable{
	private static final long serialVersionUID = -6534183385618816610L;
	private final UUID orderId;
	private final UUID productId;
}
