package br.com.example.microservice.shipping.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor @Value @Builder
public class BusinessError {
    private final String name;
    private final BusinessErrorCode code;
    private final String message;
}