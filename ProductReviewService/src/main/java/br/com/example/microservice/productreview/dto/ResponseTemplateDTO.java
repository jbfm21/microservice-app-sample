package br.com.example.microservice.productreview.dto;

import br.com.example.microservice.productreview.domain.ProductReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTemplateDTO {

	private ProductReview productReview;
	private Product product;
}
