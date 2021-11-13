package br.com.example.microservice.productreview.dto;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

public enum ProductReviewDTO 
{;
	private interface ProductReviewId { UUID getProductReviewId(); }
    private interface ProductId { UUID getProductId(); }
    private interface AuthorName { String getAuthorName(); }
    private interface Review { String getReview(); }
    private interface Product { ProductDTO getProduct(); }
    public enum Request{;
    	
        @Data @NoArgsConstructor public static class Create implements ProductId, AuthorName, Review {
        	UUID productId;
            String authorName;
            String review;
        }
        @Data @NoArgsConstructor public static class Update implements ProductReviewId, ProductId, AuthorName, Review {
        	UUID productReviewId;
        	UUID productId;
            String authorName;
            String review;
        }
    }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  public static class Public implements ProductReviewId, AuthorName, Review 
    	{
    		UUID productReviewId;
            String authorName;
            String review;
        }
    	@Data @NoArgsConstructor  public static class PublicWithProduct implements ProductReviewId, Product , AuthorName, Review 
    	{
    		UUID productReviewId;
            ProductDTO product;
            String authorName;
            String review;
        }
    }
}
