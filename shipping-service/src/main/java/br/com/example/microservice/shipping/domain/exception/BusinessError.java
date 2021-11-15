package br.com.example.microservice.shipping.domain.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString @EqualsAndHashCode
public class BusinessError {
    private final String name;
    private final BusinessErrorCode code;
    private final String message;
}