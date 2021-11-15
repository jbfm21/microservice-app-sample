package br.com.example.microservice.order.client.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CardDetailsDTO 
{
    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}