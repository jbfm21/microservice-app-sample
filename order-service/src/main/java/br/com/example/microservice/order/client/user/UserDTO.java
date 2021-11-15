package br.com.example.microservice.order.client.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO 
{
	UUID userId;
    String firstName;
    String lastName;
    CardDetailsDTO cardDetails;    		
}