package br.com.example.microservice.productreview.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.example.microservice.productreview.dto.ProductDTO;
import br.com.example.microservice.productreview.infraestructure.AppConstants;
import br.com.example.microservice.productreview.infraestructure.exceptions.CustomRestExceptions.MicroserviceUnavailableException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "catalog-product-service")
public interface ProductServiceClient {

	@GetMapping("/products/{id}")
	@CircuitBreaker(name=AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	@RateLimiter(name = AppConstants.Services.ProductService)
	@Bulkhead(name = AppConstants.Services.ProductService)
	@Retry(name = AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	//nao suportado: @TimeLimiter(name = AppConstants.Services.ProductService)	
	ProductDTO getProductById(@PathVariable Long id);
	
	default ProductDTO fallbackProductService(Long id, Exception ex) 
	{
		throw new MicroserviceUnavailableException();
	}
}
