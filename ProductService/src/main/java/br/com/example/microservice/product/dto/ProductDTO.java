package br.com.example.microservice.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public enum ProductDTO 
{;
    private interface ProductId { @Positive Long getProductId(); }
    private interface ProductName { @NotBlank String getProductName(); }
    private interface ShortDescription { @NotBlank String getShortDescription(); }
    private interface LongDescription { @NotBlank String getLongDescription(); }
    private interface InventoryId { String getInventoryId(); }

    public enum Request{;
        @Data @NoArgsConstructor public static class Create implements ProductName, ShortDescription, LongDescription, InventoryId {
            String productName;
            String shortDescription;
            String  longDescription;
            String  inventoryId;
        }
        @Data @NoArgsConstructor public static class Update implements ProductId, ProductName, ShortDescription, LongDescription, InventoryId {
            Long productId;
            String productName;
            String shortDescription;
            String  longDescription;
            String  inventoryId;
        }
    }

    public enum Response{;
    	@Data @NoArgsConstructor  public static class Public implements ProductId, ProductName, ShortDescription, LongDescription {
            Long productId;
            String productName;
            String shortDescription;
            String  longDescription;
        }
    }
}