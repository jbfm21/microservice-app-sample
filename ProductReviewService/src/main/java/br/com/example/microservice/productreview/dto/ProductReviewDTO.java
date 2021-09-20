package br.com.example.microservice.productreview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public enum ProductReviewDTO 
{;
	private interface ProductReviewId { Long getProductReviewId(); }
    private interface ProductId { Long getProductId(); }
    private interface AuthorName { String getAuthorName(); }
    private interface Review { String getReview(); }
    private interface Product { ProductDTO getProduct(); }
    public enum Request{;
    	
        @Data @NoArgsConstructor public static class Create implements ProductId, AuthorName, Review {
            Long productId;
            String authorName;
            String review;
        }
        @Data @NoArgsConstructor public static class Update implements ProductReviewId, ProductId, AuthorName, Review {
        	Long productReviewId;
            Long productId;
            String authorName;
            String review;
        }
    }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  public static class Public implements ProductReviewId, AuthorName, Review 
    	{
        	Long productReviewId;
            String authorName;
            String review;
        }
    	@Data @NoArgsConstructor  public static class PublicWithProduct implements ProductReviewId, Product , AuthorName, Review 
    	{
        	Long productReviewId;
            ProductDTO product;
            String authorName;
            String review;
        }
    }
}
