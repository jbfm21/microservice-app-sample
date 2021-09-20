package br.com.example.microservice.productreview.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.example.microservice.productreview.dto.ProductDTO;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

	@GetMapping("/products/{id}")
	ProductDTO getProductById(@PathVariable Long id);
}
