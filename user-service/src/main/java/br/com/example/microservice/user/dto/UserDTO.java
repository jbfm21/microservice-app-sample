package br.com.example.microservice.user.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum UserDTO 
{;
    private interface UserId { @NotBlank UUID getUserId(); }
	private interface FirstNameId { @NotBlank String getLastName(); }
	private interface LastNameId { @NotBlank  String getFirstName(); }
	private interface Email { @NotBlank  String getEmail(); }
	private interface CardDetails { @NotBlank  CardDetailsDTO.Response.Public getCardDetails(); }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements UserId, FirstNameId, LastNameId, Email, CardDetails {
    		UUID userId;
    	    String firstName;
    	    String lastName;
    	    String email;
    	    CardDetailsDTO.Response.Public cardDetails;    		
    		
        }
    }
}