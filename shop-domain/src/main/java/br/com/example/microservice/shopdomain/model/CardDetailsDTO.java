package br.com.example.microservice.shopdomain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;


@Jacksonized @Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CardDetailsDTO 
{
    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}