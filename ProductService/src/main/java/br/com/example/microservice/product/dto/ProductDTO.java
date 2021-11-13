package br.com.example.microservice.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;

public enum ProductDTO 
{;
    private interface ProductId { @Positive UUID getProductId(); }
    private interface ProductName { @NotBlank String getProductName(); }
    private interface ShortDescription { @NotBlank String getShortDescription(); }
    private interface LongDescription { @NotBlank String getLongDescription(); }
    private interface Price  { @NotNull BigDecimal getPrice(); }
    
    public enum Request{;
    	
        @Data @NoArgsConstructor public static class Create implements ProductName, ShortDescription, LongDescription, Price {
            String productName;
            String shortDescription;
            String longDescription;
            BigDecimal price;
        }
        @Data @NoArgsConstructor public static class Update implements ProductId, ProductName, ShortDescription, LongDescription, Price {
            UUID productId;
            String productName;
            String shortDescription;
            String longDescription;
            BigDecimal price;
        }
    }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  public static class Public implements ProductId, ProductName, ShortDescription, LongDescription, Price {
            UUID productId;
            String productName;
            String shortDescription;
            String longDescription;
            BigDecimal price;
        }
    }
}