package br.com.example.microservice.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum CardDetailsDTO 
{;
    private interface Name { @NotBlank String getName(); }
	private interface CardNumber { @NotBlank String getCardNumber(); }
	private interface ValidUntilMonth { @NotBlank  Integer getValidUntilMonth(); }
	private interface ValidUntilYear { @NotBlank  Integer getValidUntilYear(); }
	private interface CVV { @NotBlank  Integer getCvv(); }
	
	 public enum Request{;
	    	
        @Data @NoArgsConstructor public static class Create implements Name, CardNumber, ValidUntilMonth,ValidUntilYear, CVV {
        	private String name;
     	    private String cardNumber;
     	    private Integer validUntilMonth;
     	    private Integer validUntilYear;
     	    private Integer cvv;
        }
	      
	}

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements Name, CardNumber, ValidUntilMonth,ValidUntilYear, CVV {
    	    private String name;
    	    private String cardNumber;
    	    private Integer validUntilMonth;
    	    private Integer validUntilYear;
    	    private Integer cvv;
        }
    }
}