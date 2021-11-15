package br.com.example.microservice.payment.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @AllArgsConstructor @Getter @Builder @ToString @EqualsAndHashCode
public class InvoiceCreatedEvent {

	 public final UUID paymentId;
	 public final UUID orderId;
}
