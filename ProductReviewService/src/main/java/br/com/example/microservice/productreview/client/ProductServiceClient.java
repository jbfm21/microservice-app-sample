package br.com.example.microservice.productreview.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.example.microservice.infraestructure.exceptions.MicroserviceUnavailableException;
import br.com.example.microservice.productreview.dto.ProductDTO;
import br.com.example.microservice.productreview.infraestructure.AppConstants;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

//TODO: The best practice is to call microservice directly or api-gatway? 
@FeignClient(name = "catalog-product-service")
public interface ProductServiceClient {

	@GetMapping("/products/{id}")
	@CircuitBreaker(name=AppConstants.Services.PRODUCT_SERVICE, fallbackMethod = "fallbackProductService")
	@RateLimiter(name = AppConstants.Services.PRODUCT_SERVICE)
	@Bulkhead(name = AppConstants.Services.PRODUCT_SERVICE)
	@Retry(name = AppConstants.Services.PRODUCT_SERVICE, fallbackMethod = "fallbackProductService")
	//nao suportado: @TimeLimiter(name = AppConstants.Services.ProductService)	
	ProductDTO getProductById(@PathVariable UUID id);
	
	default ProductDTO fallbackProductService(UUID id, Exception ex) 
	{
		throw new MicroserviceUnavailableException();
	}
}
