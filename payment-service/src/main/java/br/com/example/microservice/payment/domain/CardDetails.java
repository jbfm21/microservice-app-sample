package br.com.example.microservice.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value @AllArgsConstructor
public class CardDetails {

    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}
